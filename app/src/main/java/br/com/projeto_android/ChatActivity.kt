package br.com.projeto_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerViewMensagens: RecyclerView
    private lateinit var editTextMensagem: TextInputEditText
    private lateinit var buttonEnviar: MaterialButton
    private lateinit var mensagensAdapter: MensagensAdapter

    // Lista de mensagens mockadas
    private val listaMensagens = mutableListOf<Mensagem>(
        Mensagem("Olá, como está o pedido?", true),
        Mensagem("Estamos processando seu pedido, logo mais estará pronto.", false),
        Mensagem("Ótimo, obrigado pelo retorno!", true),
        Mensagem("Estamos aqui para ajudar. Se precisar de algo mais, estamos à disposição.", false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Configurando a Toolbar
        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)
        val nomePedido = intent.getStringExtra("NOME_PEDIDO") ?: "Chat"
        toolbar.title = nomePedido

        // Configurar RecyclerView
        recyclerViewMensagens = findViewById(R.id.recyclerViewMensagens)
        recyclerViewMensagens.layoutManager = LinearLayoutManager(this)
        mensagensAdapter = MensagensAdapter(listaMensagens)
        recyclerViewMensagens.adapter = mensagensAdapter

        // Configurar campo de texto e botão de enviar
        editTextMensagem = findViewById(R.id.editTextMensagem)
        buttonEnviar = findViewById(R.id.buttonEnviar)

        buttonEnviar.setOnClickListener {
            val mensagemTexto = editTextMensagem.text.toString()
            if (mensagemTexto.isNotEmpty()) {
                enviarMensagem(mensagemTexto)
                editTextMensagem.text?.clear()
            }
        }
    }

    private fun enviarMensagem(mensagem: String) {
        listaMensagens.add(Mensagem(mensagem, true))  // Adiciona a mensagem do usuário
        mensagensAdapter.notifyItemInserted(listaMensagens.size - 1)
        recyclerViewMensagens.scrollToPosition(listaMensagens.size - 1)

        // Aqui você pode simular uma resposta automática da loja, se desejar
    }
}

data class Mensagem(val texto: String, val isUser: Boolean)

class MensagensAdapter(private val mensagens: List<Mensagem>) :
    RecyclerView.Adapter<MensagensAdapter.MensagemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensagemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            if (viewType == VIEW_TYPE_USER) R.layout.msg_usuario
            else R.layout.msg_loja, parent, false)
        return MensagemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MensagemViewHolder, position: Int) {
        val mensagem = mensagens[position]
        holder.textoMensagem.text = mensagem.texto
    }

    override fun getItemViewType(position: Int): Int {
        return if (mensagens[position].isUser) VIEW_TYPE_USER else VIEW_TYPE_LOJA
    }

    override fun getItemCount(): Int = mensagens.size

    inner class MensagemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textoMensagem: TextView = itemView.findViewById(R.id.textViewMensagem)
    }

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_LOJA = 2
    }
}
