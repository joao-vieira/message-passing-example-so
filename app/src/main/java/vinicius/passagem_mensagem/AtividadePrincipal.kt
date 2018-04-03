package vinicius.passagem_mensagem

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import java.util.Random

class AtividadePrincipal : AppCompatActivity() {

    var runThreadProdutora: Runnable? = null
    val rNumeroAleatorioGerado = Random()
    var iNumeroAleatorioCapturado = 0
    var bProsseguirProduzindo = true
    var iOrigemMensagemProduzida = -1;
    var iMensagemProduzidaEmMemoria = -1
    var iToken = 0
    var runThreadConsumidora: Runnable? = null
    var bProsseguirConsumindo = true
    var iMensagemRecebida = -1
    var alNumerosRecebidos: ArrayList<Int> = ArrayList()

    fun enviarMensagem(origem: Int, iMensagem: Int) {
        iOrigemMensagemProduzida = origem
        iMensagemProduzidaEmMemoria = iMensagem
        bProsseguirProduzindo = false
    }

    fun int ReceberMensagem(origem: Int){
        if (origem == iOrigemMensagemProduzida) and (iMensagemProduzidaEmMemoria > -1){
            bProsseguirConsumindo = true
            return iMensagemProduzidaEmMemoria
        }
    }
	
    fun RetornaMensagem(mensagem: int){
        bProsseguirProduzindo = true
        bProsseguirConsumindo = false
        iMensagemRecebida = mensagem
    }
	
    fun boolean VerificaRetorno(){
	    return iMensagemProduzidaEmMemoria == iMensagemRecebida
    }

    fun iniciarThreadProdutora() {
        runThreadProdutora = Runnable {
            if (VerificaRetorno() and bProsseguirProduzindo) {
                iNumeroAleatorioCapturado = rNumeroAleatorioGerado.nextInt(100)
                enviarMensagem(1, iNumeroAleatorioCapturado)
            } 
			else if (bProsseguirProduzindo) {
				enviarMensagem(1, iNumeroAleatorioCapturado)
			}
            Handler().postDelayed(runThreadProdutora, 1000)
        }
        Handler().post(runThreadProdutora)
    }

    fun iniciarThreadConsumidora() {
        runThreadConsumidora = Runnable {
            val MensagemRecebida = ReceberMensagem(1)
            if (bProsseguirConsumindo) {
                alNumerosRecebidos.add(MensagemRecebida!!)
                RetornaMensagem(MensagemRecebida)
                println("CONSUMIDOS: " + alNumerosRecebidos)
            }
			else {
				RetornaMensagem(-1)
			}
            Handler().postDelayed(runThreadConsumidora, 500)
        }
        Handler().post(runThreadConsumidora)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.atividade_principal)

        iniciarThreadProdutora()
        iniciarThreadConsumidora()
    }
}
