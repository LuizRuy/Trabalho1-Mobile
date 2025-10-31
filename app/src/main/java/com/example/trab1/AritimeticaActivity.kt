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
    private var rodadaAtual = 0
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

        proximaRodada()

        buttonResposta.setOnClickListener {
            verificarResposta()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun proximaRodada() {
        if(rodadaAtual == maxRodadas){
            finalizarJogo()
            return
        }
        rodadaAtual++
        rodada.text = "Rodada: $rodadaAtual/$maxRodadas"

        //Garantir que os numeros nunca se repitam
        val op1 = (0..9).random()
        val op2 = (0..9).random()
        val isSoma = Random.nextBoolean()

        numero1.text = op1.toString()
        numero2.text = op2.toString()

        resposta.text.clear()

        if(isSoma){
            operador.text = "+"
            respostaCorreta = op1 + op2
        }else{
            operador.text = "-"
            respostaCorreta = op1 - op2

        }

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