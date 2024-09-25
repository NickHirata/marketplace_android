package br.com.projeto_android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class AlterarSenhaActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var savePasswordButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alterar_senha)

        // Configurar a Toolbar
        toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Mostrar o botão de voltar

        // Configurar o botão Salvar Senha
        savePasswordButton = findViewById(R.id.save_password_button)
        savePasswordButton.setOnClickListener {
            Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
            // Aqui você pode adicionar a lógica para alterar a senha do usuário
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // Fecha a Activity atual e volta para a anterior
        return true
    }
}
