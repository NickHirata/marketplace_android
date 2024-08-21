package br.com.projeto_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Definição da classe Pedido
data class Pedido(val status: String)

// Definição do adaptador para a RecyclerView
class PedidosAdapter(private val pedidos: List<Pedido>) :
    RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder>() {

    // Criação do ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    // Ligação dos dados com o ViewHolder
    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.statusTextView.text = pedido.status

        holder.chatButton.setOnClickListener {
            // Implementar lógica para abrir o chat
        }

        holder.avaliacaoButton.setOnClickListener {
            // Implementar lógica para abrir a tela de avaliação
        }
    }

    // Retorna o número de itens na lista
    override fun getItemCount(): Int = pedidos.size

    // Definição do ViewHolder para os itens da lista
    inner class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusTextView: TextView = itemView.findViewById(R.id.textViewStatus)
        val chatButton: ImageButton = itemView.findViewById(R.id.buttonChat)
        val avaliacaoButton: ImageButton = itemView.findViewById(R.id.buttonAvaliacao)
    }
}
