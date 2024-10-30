package models

data class Chat(
    val id: String = "",
    val dataHoraEnvio: String = "",  // Formato de data e hora (por exemplo, ISO 8601)
    val nomeUsuario: String = "",
    val mensagem: String = "",
    val idPedido: String = ""
)

