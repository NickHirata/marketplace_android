package models

data class Empresa(
    var id: String = "",
    val nomeFantasia: String = "",
    val email: String = "",
    val cnpj: String = "",
    val endereco: String = "",
    val telefone: String = ""
)
