package br.com.projeto_android

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class PerfilActivity : AppCompatActivity() {

    private lateinit var btnEditarPerfil: MaterialButton
    private lateinit var btnListarClientes: MaterialButton
    private lateinit var btnListarUsuarios: MaterialButton
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbar: MaterialToolbar

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

        // Ações dos botões
        btnEditarPerfil.setOnClickListener {
            Toast.makeText(this, "Editando perfil...", Toast.LENGTH_SHORT).show()
            // Lógica para abrir a tela de edição de perfil
        }

        btnListarClientes.setOnClickListener {
            Toast.makeText(this, "Listando clientes...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ListaClientesActivity::class.java)
            startActivity(intent)
        }

        btnListarUsuarios.setOnClickListener {
            Toast.makeText(this, "Listando usuários...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ListaFuncionariosActivity::class.java)
            startActivity(intent)
        }

        // Configurar BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    // Lógica para a Home
                    Toast.makeText(this, "Indo para Home...", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MenuClienteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_profile -> {
                    // Já está no perfil, não precisa de ação

                    Toast.makeText(this, "Você já está no perfil.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, PerfilActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_logout -> {
                    // Lógica para logout
                    Toast.makeText(this, "Saindo...", Toast.LENGTH_SHORT).show()
                    // Implementar o logout aqui
                    true
                }
                else -> false
            }
        }
    }
}
