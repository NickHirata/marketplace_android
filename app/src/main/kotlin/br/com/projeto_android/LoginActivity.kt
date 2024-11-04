package br.com.projeto_android

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import models.Login

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var registerLink: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializando os componentes
        emailInputLayout = findViewById(R.id.emailInputLayout)
        emailEditText = findViewById(R.id.emailEditText)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerLink = findViewById(R.id.registerLink)

        // Inicializando o Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Configurando o clique do botão de login
        loginButton.setOnClickListener {
            validateAndLogin()
        }

        // Configurando o clique do link de cadastro
        registerLink.setOnClickListener {
            val intent = Intent(this, RegistroEmpresaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateAndLogin() {
        val identifier = emailEditText.text.toString().trim() // Pode ser CNPJ ou email
        val password = passwordEditText.text.toString().trim()

        if (identifier.isEmpty()) {
            emailInputLayout.error = "CNPJ ou Email é obrigatório"
            return
        } else {
            emailInputLayout.error = null
        }

        if (password.isEmpty()) {
            passwordInputLayout.error = "Senha é obrigatória"
            return
        } else {
            passwordInputLayout.error = null
        }

        authenticateUser(identifier, password)
    }

    private fun authenticateUser(identifier: String, password: String) {
        db.collection("login")
            .whereEqualTo("cnpj_email", identifier)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "CNPJ ou Email não encontrado", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val loginData = documents.first().toObject(Login::class.java)

                // Verifica se a senha corresponde
                if (loginData.senha == password) {
                    if (loginData.id_empresa != null) {
                        saveCompanyId(loginData.id_empresa, loginData.id_usuario?: "")
                        Toast.makeText(this, "Login da empresa realizado com sucesso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MenuClienteActivity::class.java)
                        startActivity(intent)
                    } else if (loginData.id_usuario != null) {
                        saveCompanyId(loginData.id_empresa ?: "", loginData.id_usuario?: "")
                        Toast.makeText(this, "Login do funcionário realizado com sucesso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MenuClienteActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao autenticar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveCompanyId(idEmpresa: String, idUsuario: String) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("id_empresa", idEmpresa)
        editor.putString("id_usuario", idUsuario)
        editor.apply()
    }
}
