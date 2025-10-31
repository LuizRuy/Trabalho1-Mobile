package com.example.trab1

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class ContagemActivity : AppCompatActivity() {

    private lateinit var imageViewFigura: ImageView
    private lateinit var textViewRodada: TextView
    private lateinit var botoesOpcao: List<Button>
    private lateinit var todasAsPerguntas: List<Pergunta>
    private lateinit var perguntasDaPartida: List<Pergunta>
    private var rodadaAtual = 0
    private var acertos = 0
    private val maxRodadas = 5


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contagem)

        imageViewFigura = findViewById(R.id.imageViewFigura)
        textViewRodada = findViewById(R.id.textViewRodadaContagem)
        botoesOpcao = listOf(
            findViewById(R.id.buttonOpcao1),
            findViewById(R.id.buttonOpcao2),
            findViewById(R.id.buttonOpcao3)
        )

        inicializarPerguntas()
        iniciarPartida()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun inicializarPerguntas(){
        todasAsPerguntas = listOf(
            Pergunta(R.drawable.img1, 3),
            Pergunta(R.drawable.img2, 7),
            Pergunta(R.drawable.img3, 11)

        )
    }

    private fun iniciarPartida(){
        acertos = 0
        rodadaAtual = 0
        perguntasDaPartida = todasAsPerguntas.shuffled().take(maxRodadas)
        proximaRodada()
    }

    private fun proximaRodada(){
        if(rodadaAtual == maxRodadas){
            finalizarJogo()
            return
        }
        rodadaAtual++
        textViewRodada.text = "Rodada: $rodadaAtual/$maxRodadas"
        val perguntaAtual = perguntasDaPartida[rodadaAtual]
        imageViewFigura.setImageResource(perguntaAtual.idImagen)

        val respostaCerta = perguntaAtual.respostaCorreta
        val opcoes = mutableListOf(respostaCerta)

        while(opcoes.size < 3){
            val respostaErrada = Random.nextInt(1,20)
            opcoes.add(respostaErrada)
        }

        val opcoesAleatorias = opcoes.shuffled()
        botoesOpcao.forEachIndexed { index, button ->
            val opcao = opcoesAleatorias[index]
            button.text = opcao.toString()
            button.setOnClickListener {
                verificarResposta(opcao, respostaCerta)
            }
        }

    }

    private fun verificarResposta(respostaUsuario: Int, respostaCerta: Int){
        val builder = AlertDialog.Builder(this)
        if(respostaUsuario == respostaCerta){
            acertos++
            builder.setTitle("Correto!")
            builder.setMessage("Sua resposta está correta!")
        }else{
            builder.setTitle("Errou!")
            builder.setMessage("A resposta correta era: $respostaCerta")
        }

        builder.setPositiveButton("Próxima"){ dialog, which ->
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