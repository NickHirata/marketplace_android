package br.com.projeto_android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegistroClienteActivity : AppCompatActivity() {

    private lateinit var clientNameInputLayout:TextInputLayout
    private lateinit var clientNameEditText: TextInputEditText
    private lateinit var clientEmailInputLayout: TextInputLayout
    private lateinit var clientEmailEditText: TextInputEditText
    private lateinit var clientPhoneInputLayout: TextInputLayout
    private lateinit var clientPhoneEditText: TextInputEditText
    private lateinit var registerClientButton: MaterialButton

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

        // Aqui você pode adicionar a lógica para validação dos campos.
        // Por exemplo, verificar se os campos estão vazios.

        // Exemplo de mensagem de sucesso
        Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()

        // Redirecionar paraa próxima Activity ou tela
        // finish() // Retorna à tela anterior ou pode redirecionar conforme a necessidade
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // Fecha a Activity atual e volta para a anterior
        return true
    }
}