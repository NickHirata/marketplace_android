package br.com.projeto_android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import models.Cliente

class RegistroClienteActivity : AppCompatActivity() {

    private lateinit var clientNameInputLayout: TextInputLayout
    private lateinit var clientNameEditText: TextInputEditText
    private lateinit var clientEmailInputLayout: TextInputLayout
    private lateinit var clientEmailEditText: TextInputEditText
    private lateinit var clientPhoneInputLayout: TextInputLayout
    private lateinit var clientPhoneEditText: TextInputEditText
    private lateinit var registerClientButton: MaterialButton

    private val db = FirebaseFirestore.getInstance() // Instanciando o Firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro_cliente)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Inicializando os componentes
        clientNameInputLayout = findViewById(R.id.clientNameInputLayout)
        clientNameEditText = findViewById(R.id.clientNameEditText)
        clientEmailInputLayout = findViewById(R.id.clientEmailInputLayout)
        clientEmailEditText = findViewById(R.id.clientEmailEditText)
        clientPhoneInputLayout = findViewById(R.id.clientPhoneInputLayout)
        clientPhoneEditText = findViewById(R.id.clientPhoneEditText)
        registerClientButton = findViewById(R.id.registerClientButton)

        // Configurando o clique do botão de cadastro
        registerClientButton.setOnClickListener {
            validateAndRegisterClient()
        }
    }

    private fun validateAndRegisterClient() {
        val clientName = clientNameEditText.text.toString().trim()
        val email = clientEmailEditText.text.toString().trim()
        val phone = clientPhoneEditText.text.toString().trim()

        // Validação dos campos
        if (clientName.isEmpty()) {
            clientNameInputLayout.error = "Nome é obrigatório"
            return
        } else {
            clientNameInputLayout.error = null
        }

        if (email.isEmpty()) {
            clientEmailInputLayout.error = "Email é obrigatório"
            return
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            clientEmailInputLayout.error = "Email inválido"
            return
        } else {
            clientEmailInputLayout.error = null
        }

        if (phone.isEmpty()) {
            clientPhoneInputLayout.error = "Telefone é obrigatório"
            return
        } else {
            clientPhoneInputLayout.error = null
        }

        // Recuperar o ID da empresa das SharedPreferences
        val idEmpresa = getLoggedCompanyId()

        // Criar o objeto Cliente para enviar ao Firestore
        val cliente = Cliente(
            nome = clientName,
            email = email,
            telefone = phone,
            empresa = idEmpresa // Adicionando o idEmpresa ao cliente
        )

        // Salvar no Firestore
        db.collection("clientes") // Coleção "clientes" no Firestore
            .add(cliente) // Adiciona o cliente ao Firestore
            .addOnSuccessListener {
                // Exibe a mensagem de sucesso
                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()

                // Redireciona para a tela anterior ou realiza outro tipo de ação
                finish()
            }
            .addOnFailureListener { e ->
                // Exibe a mensagem de erro
                Toast.makeText(this, "Erro ao cadastrar cliente: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Função para obter o ID da empresa das SharedPreferences
    private fun getLoggedCompanyId(): String {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("id_empresa", "") ?: ""
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // Fecha a Activity atual e volta para a anterior
        return true
    }
}
