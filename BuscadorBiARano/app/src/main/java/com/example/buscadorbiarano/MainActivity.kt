package com.example.buscadorbiarano

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buscadorbiarano.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // El adaptador que gestiona cómo se muestran los datos en la lista
    private lateinit var adapter: CryptoAdapter

    // Lista "maestra" que guarda todas las criptos descargadas para poder filtrar sin repetir la petición a internet
    private var allCryptos: List<Crypto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflamos el diseño de la actividad
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        initSearchView()
        loadCryptos()
    }

    // Configura el RecyclerView )el contenedor de la lista)
    private fun initRecyclerView() {
        // Indicamos que la lista se muestre de forma vertical
        binding.rvCryptos.layoutManager = LinearLayoutManager(this)

        // Inicializamos el adaptador con una lista vacía y definimos qué pasa al hacer clic en la cripto que queramos
        adapter = CryptoAdapter(emptyList()) { crypto -> navigateToDetail(crypto) }
        binding.rvCryptos.adapter = adapter
    }

    // Configura el funcionamiento de la barra de búsqueda
    private fun initSearchView() {
        binding.svCrypto.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // Se ejecuta al pulsar "Enter" en el teclado (no lo usamos aquí)
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            // Se ejecuta cada vez que el usuario escribe o borra una letra
            override fun onQueryTextChange(newText: String?): Boolean {
                filterCryptos(newText ?: "")
                return true
            }
        })
    }

    // Lógica para filtrar la lista según el nombre o el símbolo
    private fun filterCryptos(query: String) {
        val filteredList = allCryptos.filter { crypto ->
            // Compara el texto escrito con el nombre o símbolo (ignorando mayúsculas/minúsculas)
            crypto.name.contains(query, ignoreCase = true) ||
                    crypto.symbol.contains(query, ignoreCase = true)
        }
        // Creamos un nuevo adaptador con los resultados filtrados y lo refrescamos en pantalla
        adapter = CryptoAdapter(filteredList) { crypto -> navigateToDetail(crypto) }
        binding.rvCryptos.adapter = adapter
    }

    // Función para conectar con la API y descargar los datos
    private fun loadCryptos() {
        // Configuramos Retrofit con la URL base y el conversor de JSON (GSON)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pro-api.coinmarketcap.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Creamos el servicio a partir de nuestra interfaz ApiService
        val service = retrofit.create(ApiService::class.java)

        // Iniciamos una Corrutina en un hilo secundario para no bloquear la pantalla
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Hacemos la llamada a la API
                val response = service.getCryptos()

                // Volvemos al hilo principal para actualizar la interfaz
                runOnUiThread {
                    if (response.isSuccessful) {
                        // Guardamos los datos en nuestra lista maestra
                        allCryptos = response.body()?.data ?: emptyList()

                        // Actualizamos el adaptador con los datos reales
                        adapter = CryptoAdapter(allCryptos) { crypto -> navigateToDetail(crypto) }
                        binding.rvCryptos.adapter = adapter
                    } else {
                        // Si la API responde pero con un error (ej. clave inválida)
                        Toast.makeText(this@MainActivity, "Error en la API: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Si hay un error de red (sin internet o servidor caído)
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Sin conexión a Internet", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Función para pasar de la lista a la pantalla de detalles
    private fun navigateToDetail(crypto: Crypto) {
        val intent = Intent(this, CryptoDetailActivity::class.java)

        // Metemos en el "paquete" todos los datos que queremos mostrar en la otra pantalla
        intent.putExtra("NAME", crypto.name)
        intent.putExtra("SYMBOL", crypto.symbol)
        intent.putExtra("PRICE", crypto.quote.USD.price)
        intent.putExtra("CAP", crypto.quote.USD.market_cap)
        // Aqui cogemos la variación del porcentaje de las últimas 24h
        intent.putExtra("CHANGE", crypto.quote.USD.percent_change_24h)
        startActivity(intent)
    }
}