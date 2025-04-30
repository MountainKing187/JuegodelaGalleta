package com.example.myapplication2

import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.window.layout.WindowMetricsCalculator
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

class MainFirstActivity : AppCompatActivity() {
    private val urlString = "http://13.219.69.20:8081/todos"
    private var contador = 0
    lateinit var cronometro: Chronometer
    lateinit var recordBtn: Button
    lateinit var tex2: TextView
    var isRunning = false
    var pauseOffset: Long = 0

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
        cronometro = findViewById(R.id.idCMmeter)
        recordBtn = findViewById(R.id.records)
        tex2 = findViewById(R.id.textView2)

        moverGalleta(galleta1)
        textoContador.text = contador.toString()

        galleta1.setOnClickListener {
            tex2.text = cronometro.text
            moverGalleta(galleta1)
            moverGalleta(galletaBomba)
            galletaBomba.visibility = View.GONE

            if (!isRunning){
                cronometro.text = "00:00"
                cronometro.base = SystemClock.elapsedRealtime() - pauseOffset
                cronometro.start()
                isRunning = true
            }

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

            cronometro.text = formatTime(getElapsedTime())
            isRunning = false
            cronometro.stop()
            cronometro.base = SystemClock.elapsedRealtime()


            contador = 0
            textoContador.text = contador.toString()
        }

        resetButton.setOnClickListener{
            moverGalleta(galleta1)
            galletaBomba.visibility = View.GONE
            contador = 0
            textoContador.text = contador.toString()
        }

        recordBtn.setOnClickListener{
            FetchJsonTask(tex2).execute(urlString)
        }

    }
    fun moverGalleta(galleta: ImageButton){
        val num_ranX = Random.nextInt(650)
        val num_ranY = Random.nextInt(70,1200)

        galleta.translationX = num_ranX.toFloat()
        galleta.translationY = num_ranY.toFloat()
    }

    private fun getElapsedTime(): Long {
        return if (isRunning) {
            SystemClock.elapsedRealtime() - cronometro.base
        } else {
            pauseOffset // Retorna el tiempo acumulado si está pausado
        }
    }

    private fun formatTime(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = (totalSeconds / 60) % 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
private class FetchJsonTask(
    private val tvResult: TextView,
) : AsyncTask<String, Void, String>() {

    override fun onPreExecute() {
        super.onPreExecute()
        tvResult.text = "Cargando..."
    }

    override fun doInBackground(vararg urls: String): String {
        val urlString = urls[0]
        val result = StringBuilder()
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.apply {
                requestMethod = "GET"
                connectTimeout = 5000
                readTimeout = 5000
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        result.append(line)
                    }
                }
            } else {
                return "Error: ${connection.responseCode}"
            }
        } catch (e: Exception) {
            return "Excepción: ${e.message}"
        }
        return result.toString()
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        tvResult.text = result
    }
}
