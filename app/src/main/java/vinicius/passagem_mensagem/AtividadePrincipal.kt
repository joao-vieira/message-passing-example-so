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
    var iMensagemProduzidaEmMemoria = -1
    var iToken = 0
    var runThreadConsumidora: Runnable? = null
    var bProsseguirConsumindo = true
    var iMensagemRecebida: Int? = null
    var alNumerosRecebidos: ArrayList<Int> = ArrayList()

    fun enviarMensagem(origem: Int, iMensagem: Int) {

        if (origem == 1) {
            iMensagemProduzidaEmMemoria = iMensagem
            bProsseguirProduzindo = false
            bProsseguirConsumindo = true
        }
        if (origem == 2) {
            bProsseguirProduzindo = true
            bProsseguirConsumindo = false
        }
    }

    fun iniciarThreadProdutora() {
        runThreadProdutora = Runnable {
            iToken = 1
            if (bProsseguirProduzindo) {
                iNumeroAleatorioCapturado = rNumeroAleatorioGerado.nextInt(100)
                enviarMensagem(iToken, iNumeroAleatorioCapturado)
            }
            Handler().postDelayed(runThreadProdutora, 1000)
        }
        Handler().post(runThreadProdutora)
    }

    fun iniciarThreadConsumidora() {
        runThreadConsumidora = Runnable {
            iToken = 2
            if (bProsseguirConsumindo) {
                iMensagemRecebida = iMensagemProduzidaEmMemoria
                alNumerosRecebidos.add(iMensagemRecebida!!)
                enviarMensagem(iToken, -1)
                println("CONSUMIDOS: " + alNumerosRecebidos)
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
