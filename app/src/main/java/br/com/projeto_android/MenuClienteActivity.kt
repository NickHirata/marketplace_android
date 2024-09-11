package br.com.projeto_android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class MenuClienteActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnSolicitarOrcamento: MaterialButton
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbar: MaterialToolbar

    private val pedidos = listOf(
        Pedido("Pendente", "Software de MarketPlace"),
        Pedido("Aceito", "CRM"),
        Pedido("Recusado", "Trelo"),
        Pedido("Concluído", "Android")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_cliente)

        // Configurar a Toolbar
        toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)

        // Configurar RecyclerView e Button
        recyclerView = findViewById(R.id.recyclerViewPedidos)
        btnSolicitarOrcamento = findViewById(R.id.btnSolicitarOrcamento)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PedidosAdapter(pedidos)

        btnSolicitarOrcamento.setOnClickListener {
            Toast.makeText(this, "Solicitando orçamento...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, OrcamentoActivity::class.java)
            startActivity(intent)

        }

        // Configurar BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    // Lógica para a Home (se já estiver na Home, pode ignorar)
                    true
                }
                R.id.menu_profile -> {
                    // Lógica para abrir o perfil do usuário
                    Toast.makeText(this, "Abrindo perfil...", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_logout -> {
                    Toast.makeText(this, "Saindo...", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}

data class Pedido(val status: String, val nome: String)

class PedidosAdapter(private val pedidos: List<Pedido>) :
    RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.statusTextView.text = pedido.status
        holder.nomeTextView.text = pedido.nome

        //redireciona para comentarios
        holder.chatButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CommentsActivity::class.java)
            intent.putExtra("NOME_PEDIDO", pedido.nome)
            context.startActivity(intent)
        }

        holder.avaliacaoButton.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, AvaliacaoActivity::class.java)
            intent.putExtra("NOME_PEDIDO", pedido.nome)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = pedidos.size

    inner class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusTextView: TextView = itemView.findViewById(R.id.textViewStatus)
        val nomeTextView: TextView = itemView.findViewById(R.id.textViewNome)
        val chatButton: ImageButton = itemView.findViewById(R.id.buttonChat)
        val avaliacaoButton: ImageButton = itemView.findViewById(R.id.buttonAvaliacao)
    }
}

