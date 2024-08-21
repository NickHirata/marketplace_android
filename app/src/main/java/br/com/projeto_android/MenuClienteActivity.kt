package br.com.projeto_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class MenuClienteActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnSolicitarOrcamento: MaterialButton
    private val pedidos = listOf(
        Pedido("Pendente"),
        Pedido("Aceito"),
        Pedido("Recusado"),
        Pedido("Concluído")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_cliente)

        recyclerView = findViewById(R.id.recyclerViewPedidos)
        btnSolicitarOrcamento = findViewById(R.id.btnSolicitarOrcamento)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PedidosAdapter(pedidos)

        btnSolicitarOrcamento.setOnClickListener {
            // Implementar lógica para solicitar orçamento
        }
    }
}

data class Pedido(val status: String)

class PedidosAdapter(private val pedidos: List<Pedido>) :
    RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.statusTextView.text = pedido.status
    }

    override fun getItemCount(): Int = pedidos.size

    inner class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusTextView: TextView = itemView.findViewById(R.id.textViewStatus)
        val chatButton: ImageButton = itemView.findViewById(R.id.buttonChat)
        val avaliacaoButton: ImageButton = itemView.findViewById(R.id.buttonAvaliacao)
    }
}