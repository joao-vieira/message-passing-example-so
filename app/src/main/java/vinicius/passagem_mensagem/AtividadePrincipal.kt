package vinicius.passagem_mensagem

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import java.util.Random

class AtividadePrincipal : AppCompatActivity() {

    var runThreadProdutora  : Runnable? = null
    var runThreadConsumidora: Runnable? = null

    var bProsseguirProduzindo = true
    var bProsseguirConsumindo = false

    var iNumeroAleatorioCapturado  : Int? = 0
    var iOrigemMensagemProduzida   : Int? = -1
    var iMensagemProduzidaEmMemoria: Int? = -1
    var iMensagemRecebida          : Int? = -1

    var alNumerosRecebidos: ArrayList<Int> = ArrayList()

    
    fun enviarMensagem(origem: Int, iMensagem: Int) {
        iOrigemMensagemProduzida    = origem    /* Armazena quem foi o responsável pelo envio da mensagem */
        iMensagemProduzidaEmMemoria = iMensagem /* Guarda na memória a msg enviada pelo responsável/origem */
        bProsseguirProduzindo       = false     /* Coloca em espera a produção de novos valores */
    }

    fun ReceberMensagem(origem: Int) : Int {
        if (origem == iOrigemMensagemProduzida && iMensagemProduzidaEmMemoria!! > -1) {
            bProsseguirConsumindo = true
            return iMensagemProduzidaEmMemoria!!
        }
        return 0
    }
    
    fun RetornaMensagem(mensagem: Int){
        bProsseguirProduzindo = true
        bProsseguirConsumindo = false
        iMensagemRecebida     = mensagem
    }
    
    fun VerificaRetorno() : Boolean {
        return iMensagemProduzidaEmMemoria == iMensagemRecebida
    }

    fun iniciarThreadProdutora() {
        runThreadProdutora = Runnable {
            /* Se a mensagem foi enviada e o destino confirmou o recebimento ele gera um novo valor */
            if (VerificaRetorno() && bProsseguirProduzindo) {
                iNumeroAleatorioCapturado = iNumeroAleatorioCapturado!! + 1
                enviarMensagem(1, iNumeroAleatorioCapturado!!)
            }
            /* Se a mensagem foi enviada porém a origem não receber uma confirmação desse recebimento ele reenvia o mesmo valor*/
            else if (bProsseguirProduzindo) {
                enviarMensagem(1, iNumeroAleatorioCapturado!!)
            }
            Handler().postDelayed(runThreadProdutora, 2000)
        }
        Handler().post(runThreadProdutora)
    }

    fun iniciarThreadConsumidora() {
        runThreadConsumidora = Runnable {
            val MensagemRecebida = ReceberMensagem(1) /* Recebe a mensagem que está em memória */

            if (bProsseguirConsumindo) {
                alNumerosRecebidos.add(MensagemRecebida!!) /* Armazena em um array a mensagem recebida */
                RetornaMensagem(MensagemRecebida) /* Retorna a mensagem confirmando para o produtor que ele pode gerar novos valores */
                println("CONSUMIDOS: " + alNumerosRecebidos)
            }
            else {
                RetornaMensagem(-1) /* Retorna -1 solicitando que a origem reenvia a mensagem, sem produzir um novo valor */
            }
            Handler().postDelayed(runThreadConsumidora, 2000)
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
