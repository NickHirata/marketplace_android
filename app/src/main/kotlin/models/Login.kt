package models

data class Login(
    val id: String = "",
    val cnpj_email: String = "",
    val senha: String = "",
    val id_empresa: String? = null,
    val id_usuario: String? = null
)

