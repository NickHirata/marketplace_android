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
class CommentsActivity : AppCompatActivity() {

    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var recyclerViewComments: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var editTextComment: EditText
    private lateinit var buttonSend: Button
    private var commentsList: MutableList<Comment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        val projectName = "Projeto XYZ"
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        titleTextView.text = "Comentários do $projectName"

        // Configurando a BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    Toast.makeText(this, "Abrindo home...", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MenuClienteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_profile -> {
                    Toast.makeText(this, "Abrindo perfil...", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_logout -> {
                    Toast.makeText(this, "Saindo...", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Inicializa o RecyclerView
        recyclerViewComments = findViewById(R.id.recyclerViewComments)
        recyclerViewComments.layoutManager = LinearLayoutManager(this)

        commentsList.add(Comment("Adamson", "Esse projeto vai iniciar na segunda dia 04/09/2024", "A"))
        commentsList.add(Comment("Liam Rubicorn", "Estamos com dúvidas sobre esse projeto precisa marcar uma reunião com o cliente", "L"))

        // Inicializa o commentsAdapter corretamente
        commentsAdapter = CommentsAdapter(commentsList)
        recyclerViewComments.adapter = commentsAdapter

        // Configurações para adicionar novo comentário
        editTextComment = findViewById(R.id.editTextComment)
        buttonSend = findViewById(R.id.buttonSend)

        // Evento de clique do botão "Send"
        buttonSend.setOnClickListener {
            val newCommentText = editTextComment.text.toString().trim()

            if (newCommentText.isNotEmpty()) {
                // Adiciona o novo comentário à lista
                val newComment = Comment("Você", newCommentText, "Y")
                commentsList.add(newComment)

                // Notifica o adapter que um novo item foi inserido
                commentsAdapter.notifyItemInserted(commentsList.size - 1)

                // Rola para o final da lista para mostrar o novo comentário
                recyclerViewComments.scrollToPosition(commentsList.size - 1)

                // Limpa o campo de texto
                editTextComment.text.clear()
            } else {
                Toast.makeText(this, "O comentário não pode estar vazio", Toast.LENGTH_SHORT).show()
            }
        }

    }
}

data class Comment(val userName: String, val commentText: String, val avatarLetter: String)

class CommentsAdapter(private val commentsList: List<Comment>) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textAvatar: TextView = view.findViewById(R.id.textAvatar)
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

        // Definindo os textos dinamicamente
        holder.textAvatar.text = comment.avatarLetter
        holder.textUserName.text = comment.userName
        holder.textComment.text = comment.commentText
    }

    override fun getItemCount() = commentsList.size
}
