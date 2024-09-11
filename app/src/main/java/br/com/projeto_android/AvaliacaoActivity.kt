package br.com.projeto_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText

class AvaliacaoActivity : AppCompatActivity() {


    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avaliacao)

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
                    // Lógica para abrir o perfil do usuário
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

        // Inicializando os componentes
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val commentEditText = findViewById<TextInputEditText>(R.id.commentEditText)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val submitButton = findViewById<Button>(R.id.submitButton)

        // Ação do botão Cancelar
        cancelButton.setOnClickListener {
            finish() // Fecha a activity
        }

        // Capturar a avaliação dada pelo usuário
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (fromUser) {
                Toast.makeText(this, "Você deu $rating estrelas", Toast.LENGTH_SHORT).show()
            }
        }
        // Ação do botão Enviar Avaliação
        submitButton.setOnClickListener {
            val rating = ratingBar.rating
            val comment = commentEditText.text.toString()

            if (rating == 0f) {
                Toast.makeText(this, "Por favor, dê uma nota.", Toast.LENGTH_SHORT).show()
            } else {
                // Aqui você pode adicionar a lógica para enviar a avaliação
                Toast.makeText(this, "Avaliação enviada! Nota: $rating \nComentário: $comment", Toast.LENGTH_LONG).show()
            }
        }
    }
}