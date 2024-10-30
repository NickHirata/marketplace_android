package service
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import models.Chat
import models.Cliente
import models.Empresa
import models.Pedido
import models.Usuario

class FirebaseService {
    private val db: FirebaseFirestore = Firebase.firestore

    // Função para adicionar uma empresa
    fun addEmpresa(empresa: Empresa, onComplete: (Boolean) -> Unit) {
        db.collection("empresas").document(empresa.id)
            .set(empresa)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // Função para obter empresas
    fun getEmpresas(onComplete: (List<Empresa>?) -> Unit) {
        db.collection("empresas")
            .get()
            .addOnSuccessListener { result ->
                val empresas = result.map { it.toObject(Empresa::class.java) }
                onComplete(empresas)
            }
            .addOnFailureListener { onComplete(null) }
    }

    // Função para adicionar um pedido
    fun addPedido(pedido: Pedido, onComplete: (Boolean) -> Unit) {
        db.collection("pedidos").document(pedido.id)
            .set(pedido)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // Função para adicionar um chat a um pedido específico
    fun addChatToPedido(pedidoId: String, chat: Chat, onComplete: (Boolean) -> Unit) {
        db.collection("pedidos").document(pedidoId).collection("chats")
            .document(chat.id)
            .set(chat)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // Função para obter chats de um pedido específico
    fun getChatsFromPedido(pedidoId: String, onComplete: (List<Chat>?) -> Unit) {
        db.collection("pedidos").document(pedidoId).collection("chats")
            .get()
            .addOnSuccessListener { result ->
                val chats = result.map { it.toObject(Chat::class.java) }
                onComplete(chats)
            }
            .addOnFailureListener { onComplete(null) }
    }

    // Outras funções CRUD para Usuario e Cliente
    fun addUsuario(usuario: Usuario, onComplete: (Boolean) -> Unit) {
        db.collection("usuarios").document(usuario.id)
            .set(usuario)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun addCliente(cliente: Cliente, onComplete: (Boolean) -> Unit) {
        db.collection("clientes").document(cliente.id)
            .set(cliente)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
