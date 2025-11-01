package com.example.trab1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class AritimeticaActivity : AppCompatActivity() {

    private lateinit var numero1: TextView
    private lateinit var numero2: TextView
    private lateinit var resposta: EditText
    private lateinit var buttonResposta: Button
    private lateinit var rodada: TextView
    private lateinit var operador: TextView
    private lateinit var perguntasDaPartida: List<Operacao>
    private var indiceAtual = 0
    private var acertos = 0
    private val maxRodadas = 5
    private var respostaCorreta = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_aritimetica)

        numero1 = findViewById<TextView>(R.id.textN1)
        numero2 = findViewById<TextView>(R.id.textN2)
        resposta = findViewById<EditText>(R.id.editTextResposta)
        buttonResposta = findViewById<Button>(R.id.buttonRespAriti)
        rodada = findViewById<TextView>(R.id.textViewRodada)
        operador = findViewById<TextView>(R.id.textOperador)

        iniciarPartida()

        buttonResposta.setOnClickListener {
            verificarResposta()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun iniciarPartida() {
        acertos = 0
        indiceAtual = 0

        val todasAsOperacoes = gerarTodasAsOperacoesUnicas()

        perguntasDaPartida = todasAsOperacoes.shuffled().take(maxRodadas)

        proximaRodada()
    }

    private fun gerarTodasAsOperacoesUnicas(): List<Operacao> {
        val operacoes = mutableSetOf<Operacao>()
        val numeros = 0..9

        for (n1 in numeros) {
            for (n2 in numeros) {
                operacoes.add(Operacao(n1, n2, '+', n1 + n2))
                operacoes.add(Operacao(n1, n2, '-', n1 - n2))
            }
        }
        return operacoes.toList()
    }

    private fun proximaRodada() {
        if(indiceAtual >= maxRodadas){
            finalizarJogo()
            return
        }

        val perguntaAtual = perguntasDaPartida[indiceAtual]
        val rodadaAtualParaExibicao = indiceAtual + 1
        rodada.text = "Rodada: $rodadaAtualParaExibicao/$maxRodadas"

        numero1.text = perguntaAtual.op1.toString()
        numero2.text = perguntaAtual.op2.toString()
        operador.text = perguntaAtual.operador.toString()
        respostaCorreta = perguntaAtual.resultado

        resposta.text.clear()

        indiceAtual++

    }

    private fun verificarResposta(){
        val respostaUsuarioStr = resposta.text.toString()
        if(respostaUsuarioStr.isEmpty()){
            return
        }
        val respostaUsuario = respostaUsuarioStr.toInt()

        val builder = AlertDialog.Builder(this)
        if(respostaUsuario == respostaCorreta){
            acertos++
            builder.setTitle("Acertou!")
            builder.setMessage("Sua resposta está correta!")
        }else{
            builder.setTitle("Errou!")
            builder.setMessage("A resposta correta era: $respostaCorreta")
        }

        builder.setPositiveButton("Próxima rodada") { dialog, which ->
            proximaRodada()
        }
        builder.setCancelable(false)
        builder.show()
    }


    private fun finalizarJogo(){
        val nota = acertos * 20
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Fim de jogo!")
        builder.setMessage("Sua nota final é: $nota")
        builder.setPositiveButton("Voltar") { dialog, which ->
            finish()
        }
        builder.setCancelable(false)
        builder.show()

    }

}