package br.com.projeto_android

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import models.Login

class AlterarSenhaActivity : AppCompatActivity() {

    private lateinit var currentPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmNewPasswordEditText: EditText
    private lateinit var savePasswordButton: MaterialButton
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alterar_senha) // Usando o XML que você forneceu

        // Inicializando as views
        currentPasswordEditText = findViewById(R.id.current_password_edit_text)
        newPasswordEditText = findViewById(R.id.new_password_edit_text)
        confirmNewPasswordEditText = findViewById(R.id.confirm_new_password_edit_text)
        savePasswordButton = findViewById(R.id.save_password_button)

        // Configurar o botão de navegação (back button)
        setSupportActionBar(findViewById(R.id.topAppBar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Ação do botão
        savePasswordButton.setOnClickListener {
            val currentPassword = currentPasswordEditText.text.toString().trim()
            val newPassword = newPasswordEditText.text.toString().trim()
            val confirmNewPassword = confirmNewPasswordEditText.text.toString().trim()

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmNewPassword) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val idUsuario = getLoggedUserId()
            val idEmpresa = getLoggedCompanyId()

            if (idUsuario.isNullOrEmpty() && idEmpresa.isNullOrEmpty()) {
                Toast.makeText(this, "Nenhum ID de usuário ou empresa encontrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Atualizar a senha no Firestore
            updatePassword(idUsuario, idEmpresa, currentPassword, newPassword)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    // Função para obter o ID do usuário das SharedPreferences
    private fun getLoggedUserId(): String? {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("id_usuario", null)
    }

    // Função para obter o ID da empresa das SharedPreferences
    private fun getLoggedCompanyId(): String? {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("id_empresa", null)
    }

    // Função para alterar a senha no Firestore
    private fun updatePassword(idUsuario: String?, idEmpresa: String?, currentPassword: String, newPassword: String) {
        val id = idUsuario ?: idEmpresa // Usar o id_usuario se não existir, usa id_empresa

        if (id != null) {
            // Definir qual coleção buscar (usuarios ou empresas)
            val collection = if (idUsuario != null) {
                "usuarios"
            } else {
                "empresas"
            }

            // Buscar o login correspondente no Firestore
            db.collection(collection)
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val login = document.toObject(Login::class.java)

                        if (login != null) {
                            if (login.senha != currentPassword) {
                                Toast.makeText(this, "A senha atual está incorreta", Toast.LENGTH_SHORT).show()
                            } else {
                                // Atualiza a senha
                                val updatedLogin = login.copy(senha = newPassword)

                                // Atualiza o Firestore com a nova senha
                                db.collection(collection)
                                    .document(id)
                                    .set(updatedLogin)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Senha alterada com sucesso", Toast.LENGTH_SHORT).show()
                                        finish() // Fecha a activity após sucesso
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Erro ao atualizar a senha: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Login não encontrado", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Documento não encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro ao buscar login: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}