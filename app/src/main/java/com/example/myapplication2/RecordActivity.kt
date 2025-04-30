package com.example.myapplication2

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RecordActivity : AppCompatActivity() {
    lateinit var back: Button
    lateinit var ranking: TextView

    private val urlString = "http://13.219.69.20:8081/todos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_record)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        back = findViewById(R.id.back)
        ranking = findViewById(R.id.listaRanking)

        FetchJsonTask(ranking).execute("http://13.219.69.20:8081/todos")

        back.setOnClickListener{
            finish()
        }
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
        super.onPostExecute(result)
        try {
            val jsonObject = org.json.JSONObject(result)
            val rankingArray = jsonObject.getJSONArray("ranking")
            val formattedResult = StringBuilder()

            for (i in 0 until rankingArray.length()) {
                val item = rankingArray.getJSONObject(i)
                val puesto = item.getInt("puesto")
                val nombre = item.getString("nombre")
                val tiempo = item.getString("tiempo")
                val galletas = item.getString("galletas")
                formattedResult.append("$puesto | $nombre | $tiempo | $galletas\n") // Nueva línea por cada elemento
            }

            tvResult.text = formattedResult.toString().trim() // Elimina el último salto de línea
        } catch (e: Exception) {
            tvResult.text = "Error al procesar el JSON: ${e.message}"
        }
    }

}