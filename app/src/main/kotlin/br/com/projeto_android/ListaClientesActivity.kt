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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_clientes)

        // Configurar a Toolbar
        toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Configurar botão para registrar clientes
        registerClientButton = findViewById(R.id.registerClientButton)
        registerClientButton.setOnClickListener {
            val intent = Intent(this, RegistroClienteActivity::class.java)
            startActivity(intent)
        }

        // Obter o ID da empresa
        val idEmpresa = getLoggedCompanyId()
        if (idEmpresa.isNotEmpty()) {
            fetchClientes(idEmpresa)
        } else {
            Toast.makeText(this, "ID da empresa não encontrado", Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchClientes(idEmpresa: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("clientes")
            .whereEqualTo("empresa", idEmpresa)  // Buscar clientes pelo campo "empresa"
            .get()
            .addOnSuccessListener { documents ->
                val clientes = documents.map { document ->
                    document.toObject(Cliente::class.java)
                }

                recyclerView = findViewById(R.id.recyclerViewClientes)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = ClienteAdapter(clientes)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao buscar clientes: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onResume() {
        super.onResume()

        val idEmpresa = getLoggedCompanyId()
        if (idEmpresa.isNotEmpty()) {
            fetchClientes(idEmpresa)
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
