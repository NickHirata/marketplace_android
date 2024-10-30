package br.com.projeto_android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class ListaClientesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var registerClientButton: MaterialButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_clientes)

        // Configurar a Toolbar
        toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Inicializar o botão de registro de cliente
        registerClientButton = findViewById(R.id.registerClientButton)

        // Dados fictícios de clientes
        val clientes = listOf(
            Cliente("Ana Silva", "ana.silva@email.com", "11 99999-1111"),
            Cliente("João Souza", "joao.souza@email.com", "11 99999-2222"),
            Cliente("Maria Santos", "maria.santos@email.com", "11 99999-3333")
        )

        // Configurar o RecyclerView
        recyclerView = findViewById(R.id.recyclerViewClientes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ClienteAdapter(clientes)

        registerClientButton.setOnClickListener {
            val intent = Intent(this, RegistroClienteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // Fecha a Activity atual e volta para a anterior
        return true
    }
}

data class Cliente(val nome: String, val email: String, val telefone: String)

class ClienteAdapter(private val clientes: List<Cliente>) :
    RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clientes[position]
        holder.nomeTextView.text = cliente.nome
        holder.emailTextView.text = cliente.email
        holder.telefoneTextView.text = cliente.telefone
    }

    override fun getItemCount(): Int = clientes.size

    inner class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.textViewNome)
        val emailTextView: TextView = itemView.findViewById(R.id.textViewEmail)
        val telefoneTextView: TextView = itemView.findViewById(R.id.textViewTelefone)
    }
}
