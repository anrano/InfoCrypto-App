package com.example.buscadorbiarano

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

// Definimos una interfaz para que Retrofit sepa qué peticiones HTTP puede hacer
interface ApiService {

    // 1. Anotación @GET: Indica que vamos a "traer" (obtener) datos.
    // El texto entre paréntesis es el "endpoint" o la ruta específica dentro de la API.
    @GET("v1/cryptocurrency/listings/latest")

    // 2. Función suspend: Significa que es una función de "suspensión".
    // Esto permite que la petición se ejecute en segundo plano sin congelar la pantalla.
    suspend fun getCryptos(

        // 3. Anotación @Header: Aquí inyectamos la clave de seguridad (API KEY que me han dado en CoinMarketCap).
        // CoinMarketCap exige que esta clave vaya en la cabecera del del mensaje.
        @Header("X-CMC_PRO_API_KEY") apiKey: String = "e9f1bf0dc1a34e8abb654bf98bc3448c"

    ): Response<CryptoResponse>
    // 4. El retorno es un objeto 'Response' que envuelve nuestra clase 'CryptoResponse'.
    // Esto nos permite saber si la petición fue exitosa o si hubo un error (como el error 404).
}