package br.com.projeto_android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import models.Empresa

class RegistroEmpresaActivity : AppCompatActivity() {

    private lateinit var companyNameInputLayout: TextInputLayout
    private lateinit var companyNameEditText: TextInputEditText
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var cnpjInputLayout: TextInputLayout
    private lateinit var cnpjEditText: TextInputEditText
    private lateinit var addressInputLayout: TextInputLayout
    private lateinit var addressEditText: TextInputEditText
    private lateinit var phoneInputLayout: TextInputLayout
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var senhaInputLayout: TextInputLayout
    private lateinit var senhaEditText: TextInputEditText

    private lateinit var registerButton: MaterialButton
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro_empresa)

        // Inicializando os componentes
        companyNameInputLayout = findViewById(R.id.companyNameInputLayout)
        companyNameEditText = findViewById(R.id.companyNameEditText)
        emailInputLayout = findViewById(R.id.emailInputLayout)
        emailEditText = findViewById(R.id.emailEditText)
        cnpjInputLayout = findViewById(R.id.cnpjInputLayout)
        cnpjEditText = findViewById(R.id.cnpjEditText)
        addressInputLayout = findViewById(R.id.addressInputLayout)
        addressEditText = findViewById(R.id.addressEditText)
        phoneInputLayout = findViewById(R.id.phoneInputLayout)
        senhaInputLayout = findViewById(R.id.passwordInputLayout)
        senhaEditText = findViewById(R.id.passwordEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        registerButton = findViewById(R.id.registerButton)

        db = FirebaseFirestore.getInstance()

        registerButton.setOnClickListener {
            validateAndRegister()
        }
    }

    private fun validateAndRegister() {
        val companyName = companyNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val cnpj = cnpjEditText.text.toString().trim()
        val address = addressEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()
        val senha = senhaEditText.text.toString()


        if (companyName.isEmpty() || email.isEmpty() || cnpj.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
            return
        }

        val empresa = Empresa(
            nomeFantasia = companyName,
            email = email,
            cnpj = cnpj,
            endereco = address,
            telefone = phone
        )

        db.collection("empresa")
            .add(empresa)
            .addOnSuccessListener { documentReference ->
                val empresaId = documentReference.id  // Obtém o ID gerado pelo Firebase
                empresa.id = empresaId  // Atribui o ID ao campo id da empresa, se necessário
                Toast.makeText(this, "Cadastro de empresa realizado com sucesso", Toast.LENGTH_SHORT).show()

                addLoginEntry(empresaId, email, cnpj, senha)  // Passa o ID da empresa para o login
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao cadastrar empresa: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun addLoginEntry(empresaId: String, email: String, cnpj: String, senha: String) {
        val loginData = hashMapOf(
            "cnpj_email" to (cnpj.ifEmpty { email }),
            "senha" to senha,
            "id_empresa" to empresaId
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
}
