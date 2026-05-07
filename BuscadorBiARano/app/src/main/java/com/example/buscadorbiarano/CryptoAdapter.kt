package com.example.buscadorbiarano

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buscadorbiarano.databinding.ItemCryptoBinding

class CryptoAdapter(
    private val cryptos: List<Crypto>,
    private val onClick: (Crypto) -> Unit
) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    class CryptoViewHolder(val binding: ItemCryptoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val binding = ItemCryptoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CryptoViewHolder(binding)
    }

    // El metodo más importante: conecta los datos de una cripto concreta con la interfaz
    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val crypto = cryptos[position] // Obtenemos la cripto que toca mostrar en esta posición

        // Asignamos los textos (Nombre, Símbolo y Precio con 2 decimales)
        holder.binding.tvName.text = crypto.name
        holder.binding.tvSymbol.text = crypto.symbol
        holder.binding.tvPrice.text = "$${String.format("%.2f", crypto.quote.USD.price)}"

        // GESTIÓN DE IMÁGENES
        // Construimos la URL del logo oficial usando el ID único que nos da la API
        val imageUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/${crypto.id}.png"

        // Usamos la librería Glide para descargar y mostrar la imagen
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.moneda) // Si tarda en cargar, muestra mi moneda dorada mondea.png
            .error(R.drawable.moneda)       // Si no hay internet, muestra mi moneda dorada mondea.png
            .into(holder.binding.ivCrypto)  // Lo mete tod en la imageview
        holder.binding.root.setOnClickListener { onClick(crypto) }
    }

    // Le dice al RecyclerView cuántos elementos tiene que dibujar en total
    override fun getItemCount(): Int = cryptos.size
}