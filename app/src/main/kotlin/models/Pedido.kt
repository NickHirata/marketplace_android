package models

data class Pedido(
    var id: String = "",
    val idEmpresa: String = "",
    val nomeProjeto: String = "",
    val descricao: String = "",
    val status: String = "",
    val avaliacao: Int = 0,
    val comentarioAvaliacao: String = "",
    val cliente: String = "",
    val chats: List<Chat> = listOf() // Opcional: pode ser gerenciado como uma subcoleção em Firebase
)
