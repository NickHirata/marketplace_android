package br.com.projeto_android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import models.Usuario

class RegistroFuncionarioActivity : AppCompatActivity() {

    private lateinit var employeeNameInputLayout: TextInputLayout
    private lateinit var employeeNameEditText: TextInputEditText
    private lateinit var employeeEmailInputLayout: TextInputLayout
    private lateinit var employeeEmailEditText: TextInputEditText
    private lateinit var employeePhoneInputLayout: TextInputLayout
    private lateinit var employeePhoneEditText: TextInputEditText
    private lateinit var employeePasswordInputLayout: TextInputLayout
    private lateinit var employeePasswordEditText: TextInputEditText
    private lateinit var employeeRePasswordInputLayout: TextInputLayout
    private lateinit var employeeRePasswordEditText: TextInputEditText
    private lateinit var registerEmployeeButton: MaterialButton
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro_funcionario)

        // Inicializando os componentes
        employeeNameInputLayout = findViewById(R.id.employeeNameInputLayout)
        employeeNameEditText = findViewById(R.id.employeeNameEditText)
        employeeEmailInputLayout = findViewById(R.id.employeeEmailInputLayout)
        employeeEmailEditText = findViewById(R.id.employeeEmailEditText)
        employeePhoneInputLayout = findViewById(R.id.employeePhoneInputLayout)
        employeePhoneEditText = findViewById(R.id.employeePhoneEditText)
        employeePasswordInputLayout = findViewById(R.id.employeePasswordInputLayout)
        employeePasswordEditText = findViewById(R.id.employeePasswordEditText)
        employeeRePasswordInputLayout = findViewById(R.id.employeeRePasswordInputLayout)
        employeeRePasswordEditText = findViewById(R.id.employeeRePasswordEditText)
        registerEmployeeButton = findViewById(R.id.registerEmployeeButton)

        // Configurando o clique do botão de cadastro
        registerEmployeeButton.setOnClickListener {
            validateAndRegister()
        }
    }

    private fun validateAndRegister() {
        val employeeName = employeeNameEditText.text.toString().trim()
        val email = employeeEmailEditText.text.toString().trim()
        val phone = employeePhoneEditText.text.toString().trim()
        val password = employeePasswordEditText.text.toString().trim()
        val rePassword = employeeRePasswordEditText.text.toString().trim()

        // Validação dos campos
        if (employeeName.isEmpty()) {
            employeeNameEditText.error = "Por favor, insira o nome"
            employeeNameEditText.requestFocus()
            return
        }
        if (email.isEmpty()) {
            employeeEmailEditText.error = "Por favor, insira o email"
            employeeEmailEditText.requestFocus()
            return
        }
        if (phone.isEmpty()) {
            employeePhoneEditText.error = "Por favor, insira o telefone"
            employeePhoneEditText.requestFocus()
            return
        }
        if (password.isEmpty()) {
            employeePasswordEditText.error = "Por favor, insira a senha"
            employeePasswordEditText.requestFocus()
            return
        }
        if (password != rePassword) {
            employeeRePasswordEditText.error = "As senhas não coincidem"
            employeeRePasswordEditText.requestFocus()
            return
        }

        // Recupera o ID da empresa
        val companyId = getLoggedCompanyId()

        val novoUsuario = Usuario(
            nome = employeeName,
            email = email,
            telefone = phone,
            empresa = companyId
        )
// Adiciona o usuário à coleção "usuarios"
        db.collection("usuarios")
            .add(novoUsuario)
            .addOnSuccessListener { documentReference ->
                val userId = documentReference.id  // Captura o ID gerado pelo Firebase para o novo usuário
                Toast.makeText(this, "Cadastro do funcionário realizado com sucesso", Toast.LENGTH_SHORT).show()
                novoUsuario.id = userId
                // Adiciona entrada na coleção "login" para autenticação
                addLoginEntry(userId, email, password, companyId)  // Passa o userId para o login
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao cadastrar funcionário: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun addLoginEntry(userId: String, email: String, password: String, companyId: String) {
        val loginData = hashMapOf(
            "cnpj_email" to email,
            "senha" to password,
            "id_usuario" to userId,
            "id_empresa" to companyId
        )

        db.collection("login")
            .add(loginData)
            .addOnSuccessListener {
                Toast.makeText(this, "Dados de login criados com sucesso", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao criar login: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getLoggedCompanyId(): String {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("id_empresa", "") ?: ""
    }
}
