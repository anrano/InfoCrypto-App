package com.example.buscadorbiarano

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.buscadorbiarano.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuramos el evento de clic en el botón "Entrar"
        binding.btnEntrar.setOnClickListener {
            // Creamos un Intent para saltar de esta pantalla a la MainActivity (la lista de criptos)
            startActivity(Intent(this, MainActivity::class.java))

            // Usamos finish() para "matar" esta actividad.
            // Esto evita que si el usuario pulsa el botón "Atrás" en su móvil,
            // regrese a la pantalla de bienvenida, lo cual sería un error.
            finish()
        }
    }
}