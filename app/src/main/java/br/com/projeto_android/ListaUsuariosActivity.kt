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

class ListaUsuariosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var registerEmployeeButton: MaterialButton


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_funcionarios)

        // Configurar a Toolbar
        toolbar = findViewById(R.id.topAppBar) // Certifique-se de que a ID corresponde
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        registerEmployeeButton = findViewById(R.id.registerEmployeeButton)

        registerEmployeeButton.setOnClickListener {
            val intent = Intent(this, RegistroFuncionarioActivity::class.java)
            startActivity(intent)
        }


        // Dados fictícios de funcionários
        val funcionarios = listOf(
            Funcionario("Carlos Oliveira", "carlos.oliveira@email.com", "11 99999-1111"),
            Funcionario("Fernanda Lima", "fernanda.lima@email.com", "11 99999-2222"),
            Funcionario("Lucas Pereira", "lucas.pereira@email.com", "11 99999-3333")
        )

        // Configurar o RecyclerView
        recyclerView = findViewById(R.id.recyclerViewFuncionarios) // Verifique se a ID existe no XML
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = FuncionarioAdapter(funcionarios)
    }
    override fun onSupportNavigateUp(): Boolean {
        finish() // Fecha a Activity atual e volta para a anterior
        return true
    }
}

data class Funcionario(val nome: String, val email: String, val telefone: String)

class FuncionarioAdapter(private val funcionarios: List<Funcionario>) :
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
