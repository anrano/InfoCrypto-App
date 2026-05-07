package com.example.buscadorbiarano

// 1. Clase raíz: Representa toda la respuesta que llega de la API.
// CoinMarketCap no envía una lista directamente, sino un objeto que contiene una lista llamada "data".
data class CryptoResponse(
    val data: List<Crypto>
)

// 2. Clase principal del objeto: Define qué datos queremos de cada criptomoneda.
data class Crypto(
    val id: Int,           // El ID único (esencial para que Glide busque el logo oficial)
    val name: String,      // Nombre (ej: Bitcoin)
    val symbol: String,    // Símbolo (ej: BTC)
    val quote: Quote       // Contenedor de la información financiera (precios, etc.)
)

// 3. Clase Quote: La API organiza los precios por moneda (USD, EUR, etc.).
// Nosotros entramos en la rama de "USD" porque con la de Euro crasheaba, puede ser error de la API.
data class Quote(
    val USD: UsdDetails
)

// 4. Clase de detalles: Aquí están los valores numéricos reales.
data class UsdDetails(
    val price: Double,              // Precio actual en dólares
    val market_cap: Double,         // Capitalización de mercado total
    val percent_change_24h: Double  // La variación de precio en el último día (24h)
)