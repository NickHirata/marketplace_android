package br.com.projeto_android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import models.Chat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommentsActivity : AppCompatActivity() {

    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var recyclerViewComments: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var editTextComment: EditText
    private lateinit var buttonSend: Button
    private var commentsList: MutableList<Chat> = mutableListOf()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var idPedido: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        // Configurar o botão de navegação (back button)
        setSupportActionBar(findViewById(R.id.topAppBar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Obter o nome do projeto e o ID do pedido
        val projectName = intent.getStringExtra("NOME_PEDIDO") ?: "Projeto"
        idPedido = intent.getStringExtra("ID_PEDIDO") ?: ""  // Recupera o ID do pedido

        val titleTextView: TextView = findViewById(R.id.titleTextView)
        titleTextView.text = "Comentários do $projectName"

        // Configurar a BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    startActivity(Intent(this, MenuClienteActivity::class.java))
                    true
                }

                R.id.menu_profile -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                    true
                }

                R.id.menu_logout -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    true
                }

                else -> false
            }
        }


        // Inicializar o RecyclerView
        recyclerViewComments = findViewById(R.id.recyclerViewComments)
        recyclerViewComments.layoutManager = LinearLayoutManager(this)

        // Inicializar o commentsAdapter
        commentsAdapter = CommentsAdapter(commentsList)
        recyclerViewComments.adapter = commentsAdapter

        // Configurações para adicionar novo comentário
        editTextComment = findViewById(R.id.editTextComment)
        buttonSend = findViewById(R.id.buttonSend)

        // Carregar os comentários (chats) do pedido
        loadComments()

        // Evento de clique do botão "Send"
        buttonSend.setOnClickListener {
            val newCommentText = editTextComment.text.toString().trim()
            if (newCommentText.isNotEmpty()) {
                sendComment(newCommentText)
            } else {
                Toast.makeText(this, "O comentário não pode estar vazio", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadComments() {
        // Busca todos os chats que possuem o idPedido correspondente
        db.collection("chat")
            .whereEqualTo("idPedido", idPedido)
            .get()
            .addOnSuccessListener { documents ->
                commentsList.clear()
                for (document in documents) {
                    val chat = document.toObject(Chat::class.java)
                    commentsList.add(chat)
                }
                commentsAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Erro ao carregar comentários: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun sendComment(mensagem: String) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val idUsuario = sharedPreferences.getString("id_usuario", "")
        var nomeUsuario: String
        val dataHoraEnvio = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date())

        if (idUsuario.isNullOrEmpty()) {
            // Se não houver id_usuario, use o nome da empresa
            val idEmpresa = sharedPreferences.getString("id_empresa", "")
            db.collection("empresa").document(idEmpresa ?: "")
                .get()
                .addOnSuccessListener { document ->
                    nomeUsuario = document.getString("nomeFantasia") ?: "Nome da Empresa"

                    // Após obter o nome da empresa, envie o comentário
                    salvarChat(mensagem, nomeUsuario, dataHoraEnvio)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao obter nome da empresa", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Se houver id_usuario, use o nome do usuário
            db.collection("usuarios").document(idUsuario)
                .get()
                .addOnSuccessListener { document ->
                    nomeUsuario = document.getString("nome") ?: "Usuário Desconhecido"

                    // Após obter o nome do usuário, envie o comentário
                    salvarChat(mensagem, nomeUsuario, dataHoraEnvio)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao obter nome do usuário", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun salvarChat(mensagem: String, nomeUsuario: String, dataHoraEnvio: String) {
        val chatMessage = Chat(
            dataHoraEnvio = dataHoraEnvio,
            nomeUsuario = nomeUsuario,
            mensagem = mensagem,
            idPedido = idPedido  // Associa o comentário ao ID do pedido
        )

        db.collection("chat")
            .add(chatMessage)
            .addOnSuccessListener { documentReference ->
                val generatedId = documentReference.id

                documentReference.update("id", generatedId)  // Atualiza o documento com o ID gerado
                chatMessage.id = generatedId  // Define o ID no objeto local também
                commentsList.add(chatMessage)
                commentsAdapter.notifyItemInserted(commentsList.size - 1)
                recyclerViewComments.scrollToPosition(commentsList.size - 1)
                editTextComment.text.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao enviar comentário: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}

class CommentsAdapter(private val commentsList: List<Chat>) :
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textUserName: TextView = view.findViewById(R.id.textUserName)
        val textComment: TextView = view.findViewById(R.id.textComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = commentsList[position]
        holder.textUserName.text = comment.nomeUsuario
        holder.textComment.text = comment.mensagem
    }

    override fun getItemCount() = commentsList.size
}
