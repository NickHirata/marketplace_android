package br.com.projeto_android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import models.Cliente

class ListaClientesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var registerClientButton: MaterialButton
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_clientes)

        // Configurar a Toolbar
        toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Inicializar o botão de registro de cliente
        registerClientButton = findViewById(R.id.registerClientButton)

        // Inicializando o RecyclerView
        recyclerView = findViewById(R.id.recyclerViewClientes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Buscar o idEmpresa das SharedPreferences
        val idEmpresa = getLoggedCompanyId()

        if (idEmpresa.isNotEmpty()) {
            // Buscar clientes filtrados por idEmpresa
            fetchClientesByEmpresa(idEmpresa)
        } else {
            Toast.makeText(this, "ID da empresa não encontrado", Toast.LENGTH_SHORT).show()
        }

        // Configurar o clique do botão de cadastro de cliente
        registerClientButton.setOnClickListener {
            val intent = Intent(this, RegistroClienteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        val idEmpresa = getLoggedCompanyId()
        if (idEmpresa.isNotEmpty()) {
            fetchClientesByEmpresa(idEmpresa)
        }
    }

    // Função para obter o ID da empresa das SharedPreferences
    private fun getLoggedCompanyId(): String {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val idEmpresa = sharedPreferences.getString("id_empresa", "")
        println("ID da empresa recuperado: $idEmpresa")  // Log para depuração
        return idEmpresa ?: ""
    }

    // Função para buscar clientes filtrados pelo idEmpresa no Firestore
    private fun fetchClientesByEmpresa(idEmpresa: String) {
        db.collection("clientes")
            .whereEqualTo("idEmpresa", idEmpresa) // Filtra os clientes pela empresa
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "Nenhum cliente encontrado para esta empresa", Toast.LENGTH_SHORT).show()
                    println("Nenhum cliente encontrado")
                } else {
                    val clientes = mutableListOf<Cliente>()
                    for (document in documents) {
                        val cliente = document.toObject(Cliente::class.java)
                        clientes.add(cliente)
                        println("Cliente encontrado: ${cliente.nome}, ${cliente.empresa}")
                    }

                    // Atualiza o RecyclerView com os clientes filtrados
                    recyclerView.adapter = ClienteAdapter(clientes)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao buscar clientes: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // Fecha a Activity atual e volta para a anterior
        return true
    }
}

data class Cliente(
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    val idEmpresa: String = "" // Adicionando o campo idEmpresa para filtro no Firestore
)

class ClienteAdapter(private val clientes: List<Cliente>) :
    RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false) // Certifique-se de que este layout existe
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
