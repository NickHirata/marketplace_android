package models

data class Usuario(
    var id: String = "",
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    val empresa: String = ""  // Referência ao ID da empresa
)
