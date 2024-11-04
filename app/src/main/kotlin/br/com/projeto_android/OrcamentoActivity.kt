package br.com.projeto_android

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.projeto_android.MainActivity.Companion.db
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import models.Pedido

class OrcamentoActivity : AppCompatActivity() {

    private lateinit var editTextNomeProjeto: TextInputEditText
    private lateinit var editTextDescricao: TextInputEditText
    private lateinit var btnSolicitarOrcamento: MaterialButton
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orcamento)

        // Configurando os campos
        editTextNomeProjeto = findViewById(R.id.editTextNomeProjeto)
        editTextDescricao = findViewById(R.id.editTextDescricao)
        btnSolicitarOrcamento = findViewById(R.id.btnSolicitarOrcamento)

        // Configurando a BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    Toast.makeText(this, "Abrindo home...", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MenuClienteActivity::class.java))
                    true
                }
                R.id.menu_profile -> {
                    Toast.makeText(this, "Abrindo perfil...", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, PerfilActivity::class.java))
                    true
                }
                R.id.menu_logout -> {
                    Toast.makeText(this, "Saindo...", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Lógica para o botão Solicitar
        btnSolicitarOrcamento.setOnClickListener {
            val nomeProjeto = editTextNomeProjeto.text.toString().trim()
            val descricao = editTextDescricao.text.toString().trim()
            val idEmpresa = getLoggedCompanyId()

            if (nomeProjeto.isEmpty() || descricao.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else if (idEmpresa.isEmpty()) {
                Toast.makeText(this, "ID da empresa não encontrado", Toast.LENGTH_SHORT).show()
            } else {
                solicitarOrcamento(idEmpresa, nomeProjeto, descricao)
            }
        }
    }

    private fun solicitarOrcamento(idEmpresa: String, nomeProjeto: String, descricao: String) {
        val novoPedido = Pedido(
            idEmpresa = idEmpresa,
            nomeProjeto = nomeProjeto,
            descricao = descricao,
            status = "Pendente"
        )
        db.collection("pedido")
            .add(novoPedido)
            .addOnSuccessListener { documentReference ->
                novoPedido.id = documentReference.id  // Atribui o ID gerado pelo Firebase ao campo id do objeto
                Toast.makeText(this, "Orçamento solicitado para $nomeProjeto com ID: ${novoPedido.id}", Toast.LENGTH_SHORT).show()
                finish() // Fecha a atividade e volta para a anterior
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao solicitar orçamento: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun getLoggedCompanyId(): String {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("id_empresa", "") ?: ""
    }
}
