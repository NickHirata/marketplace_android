package models

data class Usuario(
    val id: String = "",
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    val empresa: String = ""  // Referência ao ID da empresa
)
