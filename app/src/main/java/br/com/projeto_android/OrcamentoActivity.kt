package br.com.projeto_android

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class OrcamentoActivity : AppCompatActivity() {

    private lateinit var editTextNomeProjeto: TextInputEditText
    private lateinit var editTextDescricao: TextInputEditText
    private lateinit var btnSolicitarOrcamento: MaterialButton
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orcamento)

        // Lista de status
        val statusOptions = arrayOf("Pendente", "Aceito", "Recusado", "Concluído")

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
                    val intent = Intent(this, MenuClienteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_profile -> {
                    // Lógica para abrir o perfil do usuário
                    Toast.makeText(this, "Abrindo perfil...", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, PerfilActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_logout -> {
                    // Lógica para logout
                    Toast.makeText(this, "Saindo...", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }


        // Lógica para o botão Solicitar
        btnSolicitarOrcamento.setOnClickListener {
            val nomeProjeto = editTextNomeProjeto.text.toString()
            val descricao = editTextDescricao.text.toString()

            // Implementar a lógica para a solicitação de orçamento
            Toast.makeText(this, "Orçamento solicitado para $nomeProjeto", Toast.LENGTH_SHORT).show()
        }

    }
}

