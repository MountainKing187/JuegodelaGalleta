package com.example.myapplication2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.window.layout.WindowMetricsCalculator
import kotlin.random.Random

class MainFirstActivity : AppCompatActivity() {
    private var contador = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_first)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textoContador: TextView = findViewById(R.id.contador)
        val galleta1: ImageButton = findViewById(R.id.galletalvl1)
        val resetButton: Button = findViewById(R.id.reset)
        val galletaBomba: ImageButton = findViewById(R.id.galletaBomba)

        moverGalleta(galleta1)
        textoContador.text = contador.toString()

        galleta1.setOnClickListener {
            moverGalleta(galleta1)
            moverGalleta(galletaBomba)
            galletaBomba.visibility = View.GONE

            val probRanBomba = Random.nextInt(100)

            if (probRanBomba < 30){
                galletaBomba.visibility = View.VISIBLE
            }

            contador++
            textoContador.text = contador.toString()
        }

        galletaBomba.setOnClickListener{
            moverGalleta(galletaBomba)
            galletaBomba.visibility = View.GONE

            contador = 0
            textoContador.text = contador.toString()
        }

        resetButton.setOnClickListener{
            moverGalleta(galleta1)
            galletaBomba.visibility = View.GONE
            contador = 0
            textoContador.text = contador.toString()
        }

    }
    fun moverGalleta(galleta: ImageButton){
        val num_ranX = Random.nextInt(650)
        val num_ranY = Random.nextInt(70,1200)

        galleta.translationX = num_ranX.toFloat()
        galleta.translationY = num_ranY.toFloat()
    }
}