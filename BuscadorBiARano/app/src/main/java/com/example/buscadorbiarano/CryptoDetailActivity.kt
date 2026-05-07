package com.example.buscadorbiarano

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.buscadorbiarano.databinding.ActivityCryptoDetailBinding

class CryptoDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCryptoDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCryptoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // OBTENCION DE LOS DATOS
        // Extraemos la información que enviamos desde MainActivity a través del Intent
        // El operador ?: "Cripto" es un valor por defecto por si el nombre llegara vacío
        val name = intent.getStringExtra("NAME") ?: "Cripto"
        val symbol = intent.getStringExtra("SYMBOL") ?: ""
        val price = intent.getDoubleExtra("PRICE", 0.0)
        val marketCap = intent.getDoubleExtra("CAP", 0.0)
        val change = intent.getDoubleExtra("CHANGE", 0.0)

        // MOSTRAR DATOS EN LA INTERFAZ
        binding.tvDetailName.text = "$name ($symbol)"
        // Formateamos el precio a 2 decimales para que no salgan números demasiado largos
        binding.tvDetailPrice.text = "Precio: $${String.format("%.2f", price)}"
        // La capitalización de mercado suele ser un número gigante, lo ponemos sin decimales
        binding.tvMarketCap.text = "Cap. de Mercado: $${String.format("%.0f", marketCap)}"

        // LÓGICA DE COLOR GANANCAI O PERDIDA
        // Preparamos el texto del porcentaje con el símbolo %
        val changeText = "${String.format("%.2f", change)}%"

        if (change >= 0) {
            // Si el cambio es positivo: texto con flecha arriba y color verde
            binding.tvChange24h.text = "▲ $changeText"
            binding.tvChange24h.setTextColor(Color.parseColor("#4CAF50")) // Verde esmeralda
        } else {
            // Si el cambio es negativo: texto con flecha abajo y color rojo
            binding.tvChange24h.text = "▼ $changeText"
            binding.tvChange24h.setTextColor(Color.parseColor("#F44336")) // Rojo coral
        }

        // BOTONES DE NAVEGACIÓN
        // Al pulsar el botón "Volver" del diseño, cerramos esta pantalla
        binding.btnBack.setOnClickListener {
            finish() // Finaliza la actividad actual y vuelve a la anterior al Main
        }

    }
}