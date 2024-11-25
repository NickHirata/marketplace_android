package br.com.projeto_android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RedefinirSenhaActivity : AppCompatActivity() {

    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var resetPasswordButton: MaterialButton
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redefinir_senha)

        // Inicializar Firebase e componentes da UI
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        emailInputLayout = findViewById(R.id.emailInputLayout)
        emailEditText = findViewById(R.id.emailEditText)
        resetPasswordButton = findViewById(R.id.resetPasswordButton)

        // Configurar clique no botão de redefinição
        resetPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            if (email.isEmpty()) {
                emailInputLayout.error = "E-mail é obrigatório"
            } else {
                emailInputLayout.error = null
                verificarEEnviarEmail(email)
            }
        }
    }

    private fun verificarEEnviarEmail(email: String) {
        // Verificar no Firestore se o e-mail está cadastrado
        db.collection("login")
            .whereEqualTo("cnpj_email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // E-mail encontrado, enviar redefinição de senha
                    enviarEmailRedefinicao(email)
                } else {
                    Toast.makeText(this, "E-mail não encontrado no sistema", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao verificar e-mail: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun enviarEmailRedefinicao(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "E-mail de redefinição enviado com sucesso", Toast.LENGTH_SHORT).show()
                    finish() // Fecha a atividade após sucesso
                } else {
                    Toast.makeText(this, "Erro ao enviar e-mail: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}