package br.com.projeto_android

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.button.MaterialButton
import android.widget.TextView

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var registerLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Define o layout da Activity

        // Inicializando os componentes
        emailInputLayout = findViewById(R.id.emailInputLayout)
        emailEditText = findViewById(R.id.emailEditText)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerLink = findViewById(R.id.registerLink)

        // Configurando o clique do botão de login
        loginButton.setOnClickListener {
            validateAndLogin()
        }

        /*// Configurando o clique do link de cadastro
        registerLink.setOnClickListener {
            val intent = Intent(this, RegistroEmpresaActivity::class.java)
            startActivity(intent)
        }*/

        // Configurando o clique do link de cadastro
        registerLink.setOnClickListener {
            val intent = Intent(this, RegistroClienteActivity::class.java)
            startActivity(intent)
        }

    }

    private fun validateAndLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty()) {
            emailInputLayout.error = "Email is required"
            return
        } else {
            emailInputLayout.error = null
        }

        if (password.isEmpty()) {
            passwordInputLayout.error = "Password is required"
            return
        } else {
            passwordInputLayout.error = null
        }

        // Aqui você pode adicionar a lógica para autenticação.
        // Por exemplo, fazer uma chamada de rede para verificar o login.

        // Exemplo de mensagem de sucesso
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

        // Redirecionar para a próxima Activity ou tela
        val intent = Intent(this, MenuClienteActivity::class.java)
        startActivity(intent)
        finish()
    }
}
