package br.com.projeto_android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

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
    private lateinit var registerButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro_empresa) // Define o layout da Activity

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
        phoneEditText = findViewById(R.id.phoneEditText)
        registerButton = findViewById(R.id.registerButton)

        /*val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener {
            finish()
        }*/
        // Configurando o clique do botão de cadastro
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

       /* if (companyName.isEmpty()) {
            companyNameInputLayout.error = "Nome Fantasia é obrigatório"
            return
        } else {
            companyNameInputLayout.error = null
        }

        if (email.isEmpty()) {
            emailInputLayout.error = "E-mail é obrigatório"
            return
        } else {
            emailInputLayout.error = null
        }

        if (cnpj.isEmpty()) {
            cnpjInputLayout.error = "CNPJ é obrigatório"
            return
        } else {
            cnpjInputLayout.error = null
        }

        if (address.isEmpty()) {
            addressInputLayout.error = "Endereço é obrigatório"
            return
        } else {
            addressInputLayout.error = null
        }

        if (phone.isEmpty()) {
            phoneInputLayout.error = "Telefone é obrigatório"
            return
        } else {
            phoneInputLayout.error = null
        }*/

        // Aqui você pode adicionar a lógica para cadastro.
        // Por exemplo, fazer uma chamada de rede para enviar os dados.

        // Exemplo de mensagem de sucesso
        Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()

        // Redirecionar para a próxima Activity ou tela
        // finish() // Retorna à tela anterior ou pode redirecionar conforme a necessidade
    }
}
