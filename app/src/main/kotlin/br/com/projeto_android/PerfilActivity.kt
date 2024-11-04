package br.com.projeto_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.projeto_android.MainActivity.Companion.db
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class PerfilActivity : AppCompatActivity() {

    private lateinit var btnEditarPerfil: MaterialButton
    private lateinit var btnListarClientes: MaterialButton
    private lateinit var btnListarUsuarios: MaterialButton
    private lateinit var btnAlterarSenha: MaterialButton
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Configurar a Toolbar
        toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)

        // Configurar botões
        btnEditarPerfil = findViewById(R.id.edit_profile_button)
        btnListarClientes = findViewById(R.id.list_clients_button)
        btnListarUsuarios = findViewById(R.id.list_users_button)
        btnAlterarSenha = findViewById(R.id.open_settings_button)

        // Verificar se o id_usuario está vazio e ajustar a visibilidade do botão
        val idUsuario = getLoggedUserId()
        btnListarUsuarios.visibility = if (idUsuario.isEmpty()) View.VISIBLE else View.GONE

        // Ações dos botões
        btnEditarPerfil.setOnClickListener {
            Toast.makeText(this, "Editando perfil...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, EditarPerfilActivity::class.java)
            startActivity(intent)
        }

        btnListarClientes.setOnClickListener {
            Toast.makeText(this, "Listando clientes...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ListaClientesActivity::class.java)
            startActivity(intent)
        }

        btnAlterarSenha.setOnClickListener {
            Toast.makeText(this, "Alterar senha...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AlterarSenhaActivity::class.java)
            startActivity(intent)
        }

        btnListarUsuarios.setOnClickListener {
            Toast.makeText(this, "Listando usuários...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ListaUsuariosActivity::class.java)
            startActivity(intent)
        }

        // Configurar BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    Toast.makeText(this, "Indo para Home...", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MenuClienteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_profile -> {
                    Toast.makeText(this, "Você já está no perfil.", Toast.LENGTH_SHORT).show()
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
        // Inicializar os TextViews
        userNameTextView = findViewById(R.id.user_name)
        userEmailTextView = findViewById(R.id.user_email)

        if (idUsuario.isEmpty()) {
            val idEmpresa = getLoggedCompanyId()
            fetchEmpresaData(idEmpresa)
        } else {
            fetchUsuarioData(idUsuario)
        }
    }

    private fun fetchEmpresaData(idEmpresa: String) {
        db.collection("empresa")
            .document(idEmpresa)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val nomeFantasia = document.getString("nomeFantasia")
                    val email = document.getString("email")
                    userNameTextView.text = nomeFantasia ?: "Nome da Empresa"
                    userEmailTextView.text = email ?: "email@empresa.com"
                } else {
                    Toast.makeText(this, "Empresa não encontrada", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao buscar dados da empresa: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun fetchUsuarioData(idUsuario: String) {
        db.collection("usuarios")
            .document(idUsuario)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val nome = document.getString("nome")
                    val email = document.getString("email")
                    userNameTextView.text = nome ?: "Nome do Usuário"
                    userEmailTextView.text = email ?: "email@usuario.com"
                } else {
                    Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao buscar dados do usuário: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun getLoggedUserId(): String {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("id_usuario", "") ?: ""
    }

    private fun getLoggedCompanyId(): String {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("id_empresa", "") ?: ""
    }
}
