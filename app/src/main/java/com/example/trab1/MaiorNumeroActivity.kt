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

class MaiorNumeroActivity : AppCompatActivity() {

    private lateinit var numero1: TextView
    private lateinit var numero2: TextView
    private lateinit var numero3: TextView
    private lateinit var resposta: EditText
    private lateinit var buttonResposta: Button
    private lateinit var rodada: TextView
    private val combosUsados = mutableSetOf<List<Int>>()
    private var rodadaAtual = 0
    private var acertos = 0
    private val maxRodadas = 5
    private var respostaCorreta = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_maior_numero)

        numero1 = findViewById(R.id.textNu1)
        numero2 = findViewById(R.id.textNu2)
        numero3 = findViewById(R.id.textNu3)
        resposta = findViewById(R.id.editTextResposta)
        buttonResposta = findViewById(R.id.buttonResp)
        rodada = findViewById(R.id.textViewRodada)

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

    private fun proximaRodada(){
        if(rodadaAtual == maxRodadas){
            finalizarJogo()
            return
        }
        rodadaAtual++
        rodada.text = "Rodada: $rodadaAtual/$maxRodadas"


        var numeros: List<Int>
        var tentativas = 0
        do {
            val num1 = (0..9).random()
            val num2 = (0..9).random()
            val num3 = (0..9).random()
            numeros = listOf(num1, num2, num3)
            tentativas++
            if (tentativas > 500) break
        } while (combosUsados.contains(numeros))

        combosUsados.add(numeros)

        val num1 = numeros[0]
        val num2 = numeros[1]
        val num3 = numeros[2]

        numero1.text = num1.toString()
        numero2.text = num2.toString()
        numero3.text = num3.toString()

        resposta.text.clear()

        val listNumeros = listOf(num1, num2, num3)
        val numerosOrdenados = listNumeros.sortedDescending()
        respostaCorreta = numerosOrdenados[0] * 100 + numerosOrdenados[1] * 10 + numerosOrdenados[2]

    }

    private fun verificarResposta(){
        val respostaUsuarioStr = resposta.text.toString()
        if (respostaUsuarioStr.isEmpty()){
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
            builder.setMessage("A resposta correta é $respostaCorreta")
        }
        builder.setPositiveButton("Próxima rodada"){dialog, which ->
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