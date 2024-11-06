package br.com.projeto_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import models.Avaliacao

class AvaliacaoActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private val db = FirebaseFirestore.getInstance()
    private lateinit var nomePedidoTextView: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var commentEditText: TextInputEditText
    private lateinit var cancelButton: Button
    private lateinit var submitButton: Button
    private var idPedido: String? = null  // ID do pedido para salvar ou editar a avaliação

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avaliacao)

        // Inicializando os componentes
        nomePedidoTextView = findViewById(R.id.itemNameTextView)
        ratingBar = findViewById(R.id.ratingBar)
        commentEditText = findViewById(R.id.commentEditText)
        cancelButton = findViewById(R.id.cancelButton)
        submitButton = findViewById(R.id.submitButton)

        // Pegando o nome e o id do pedido passado pela Intent
        val nomePedido = intent.getStringExtra("nome_pedido")
        idPedido = intent.getStringExtra("id_pedido")  // Recupera o ID do pedido
        nomePedidoTextView.text = nomePedido  // Exibe o nome do pedido

        // Configurações da BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    val intent = Intent(this, MenuClienteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_profile -> {
                    val intent = Intent(this, PerfilActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_logout -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Ação do botão Cancelar
        cancelButton.setOnClickListener {
            finish() // Fecha a activity
        }

        // Ação do botão Enviar Avaliação
        submitButton.setOnClickListener {
            val rating = ratingBar.rating
            val comment = commentEditText.text.toString()

            if (rating == 0f) {
                Toast.makeText(this, "Por favor, dê uma nota.", Toast.LENGTH_SHORT).show()
            } else {
                saveAvaliacao(rating, comment)
            }
        }

        // Carregar a avaliação existente, se houver
        loadAvaliacao()
    }

    // Função para recuperar o ID do usuário ou da empresa
    private fun getLoggedUserId(): String? {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("id_usuario", null)
    }

    private fun getLoggedCompanyId(): String? {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("id_empresa", null)
    }

    // Função para carregar a avaliação existente (se houver)
    private fun loadAvaliacao() {
        val idUsuario = getLoggedUserId()
        val idEmpresa = getLoggedCompanyId()

        if (idUsuario.isNullOrEmpty() && idEmpresa.isNullOrEmpty()) {
            Toast.makeText(this, "Erro: ID do usuário ou empresa não encontrado.", Toast.LENGTH_SHORT).show()
            return
        }

        // Verifica se o ID do pedido foi passado corretamente
        if (idPedido.isNullOrEmpty()) {
            Toast.makeText(this, "Erro: ID do pedido não encontrado.", Toast.LENGTH_SHORT).show()
            return
        }

        // Busca a avaliação para o pedido específico
        db.collection("avaliacoes")
            .whereEqualTo("idPedido", idPedido)  // Consulta pela avaliação do pedido
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Se não houver avaliação, nada a fazer
                    return@addOnSuccessListener
                }

                // Caso exista avaliação, preenche os campos
                for (document in documents) {
                    val avaliacao = document.toObject(Avaliacao::class.java)
                    ratingBar.rating = avaliacao.rating  // Preenche a nota
                    commentEditText.setText(avaliacao.comentario)  // Preenche o comentário
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar avaliação: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Função para salvar ou editar a avaliação no Firestore
    private fun saveAvaliacao(rating: Float, comment: String) {
        val idUsuario = getLoggedUserId()
        val idEmpresa = getLoggedCompanyId()

        if (idUsuario.isNullOrEmpty() && idEmpresa.isNullOrEmpty()) {
            Toast.makeText(this, "Erro: ID do usuário ou empresa não encontrado.", Toast.LENGTH_SHORT).show()
            return
        }

        val avaliacao = Avaliacao(
            rating = rating,
            comentario = comment,
            idPedido = idPedido ?: ""  // Associamos a avaliação ao pedido específico
        )

        // Verifica se já existe uma avaliação para este pedido
        db.collection("avaliacoes")
            .whereEqualTo("idPedido", idPedido)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Não existe uma avaliação, então salva uma nova
                    db.collection("avaliacoes")
                        .add(avaliacao)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Avaliação enviada com sucesso!", Toast.LENGTH_SHORT).show()
                            finish()  // Fecha a activity
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erro ao enviar avaliação: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Se já existe, vamos editar a avaliação
                    val documentId = documents.first().id
                    db.collection("avaliacoes")
                        .document(documentId)
                        .set(avaliacao)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Avaliação atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                            finish()  // Fecha a activity
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erro ao atualizar avaliação: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao verificar avaliação: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
