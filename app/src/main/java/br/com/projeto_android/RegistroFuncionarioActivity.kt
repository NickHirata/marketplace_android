package br.com.projeto_android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegistroFuncionarioActivity : AppCompatActivity() {

    private lateinit var employeeNameInputLayout:TextInputLayout
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

        // Aqui você pode adicionar a lógica para validação dos campos.
        // Por exemplo, verificar se os campos estão vazios ou se a senha e a confirmação de senha coincidem.

        // Exemplo de mensagem de sucesso
        Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()

        // Redirecionar para a próxima Activity ou tela
        // finish() // Retorna à tela anterior ou pode redirecionar conforme a necessidade
    }
}