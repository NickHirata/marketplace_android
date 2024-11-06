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
import models.Usuario

class ListaUsuariosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var registerEmployeeButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_funcionarios)

        // Configurar a Toolbar
        toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        registerEmployeeButton = findViewById(R.id.registerEmployeeButton)
        registerEmployeeButton.setOnClickListener {
            val intent = Intent(this, RegistroFuncionarioActivity::class.java)
            startActivity(intent)
        }

        val idEmpresa = getLoggedCompanyId()
        if (idEmpresa.isNotEmpty()) {
            fetchEmployees(idEmpresa)
        } else {
            Toast.makeText(this, "ID da empresa não encontrado", Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchEmployees(idEmpresa: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios")
            .whereEqualTo("empresa", idEmpresa)  // Ajustado para buscar pelo campo `empresa`
            .get()
            .addOnSuccessListener { documents ->
                val funcionarios = documents.map { document ->
                    document.toObject(Usuario::class.java)
                }

                recyclerView = findViewById(R.id.recyclerViewFuncionarios)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = FuncionarioAdapter(funcionarios)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao buscar funcionários: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onResume() {
        super.onResume()

        val idEmpresa = getLoggedCompanyId()
        if (idEmpresa.isNotEmpty()) {
            fetchEmployees(idEmpresa)
        }
    }
    private fun getLoggedCompanyId(): String {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("id_empresa", "") ?: ""
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

class FuncionarioAdapter(private val funcionarios: List<Usuario>) :
    RecyclerView.Adapter<FuncionarioAdapter.FuncionarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuncionarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_funcionario, parent, false) // Certifique-se de ter esse layout
        return FuncionarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: FuncionarioViewHolder, position: Int) {
        val funcionario = funcionarios[position]
        holder.nomeTextView.text = funcionario.nome
        holder.emailTextView.text = funcionario.email
        holder.telefoneTextView.text = funcionario.telefone
    }

    override fun getItemCount(): Int = funcionarios.size

    inner class FuncionarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.textViewNome)
        val emailTextView: TextView = itemView.findViewById(R.id.textViewEmail)
        val telefoneTextView: TextView = itemView.findViewById(R.id.textViewTelefone)
    }
}
