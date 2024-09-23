package br.com.projeto_android

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.projeto_android.R.id.buttonSend
import br.com.projeto_android.R.id.emailEditText
import com.google.android.material.appbar.MaterialToolbar

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        // Configurar a Toolbar
        toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Mostrar o botão de voltar

        // Configurar EditTexts e Botão
        nameEditText = findViewById(R.id.passwordEditText)
        emailEditText = findViewById(R.id.emailEditText)
        saveButton = findViewById(buttonSend)

        // Configurar ação do botão Salvar
        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                // Lógica para salvar as informações do perfil
                // Pode ser uma chamada à API ou salvamento local
                Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                finish() // Fecha a Activity e retorna para a anterior
            } else {
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
