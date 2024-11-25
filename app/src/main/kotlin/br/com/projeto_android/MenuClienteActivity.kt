package br.com.projeto_android

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.projeto_android.MainActivity.Companion.db
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import models.Pedido
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MenuClienteActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnSolicitarOrcamento: MaterialButton
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var userNameTextView: TextView
    private val pedidosList = mutableListOf<Pedido>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_cliente)

        // Configurar a Toolbar
        toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)

        userNameTextView = findViewById(R.id.user_name)

        // Configurar RecyclerView e Button
        recyclerView = findViewById(R.id.recyclerViewPedidos)
        btnSolicitarOrcamento = findViewById(R.id.btnSolicitarOrcamento)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PedidosAdapter(pedidosList, this)  // Passa o contexto

        btnSolicitarOrcamento.setOnClickListener {
            Toast.makeText(this, "Gerando pedido...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, OrcamentoActivity::class.java)
            startActivity(intent)
        }

        // Configurar BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> true
                R.id.menu_profile -> {
                    val intent = Intent(this, PerfilActivity::class.java)
                    startActivity(intent)
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

        val idEmpresa = getLoggedCompanyId()
        if (idEmpresa.isNotEmpty()) {
            fetchEmpresaNome(idEmpresa)
            fetchPedidos(idEmpresa)
        } else {
            Toast.makeText(this, "ID da empresa não encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        val idEmpresa = getLoggedCompanyId()
        if (idEmpresa.isNotEmpty()) {
            fetchPedidos(idEmpresa)
        }
    }

    private fun fetchEmpresaNome(idEmpresa: String) {
        db.collection("empresa")
            .document(idEmpresa)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val nomeFantasia = document.getString("nomeFantasia")
                    userNameTextView.text = nomeFantasia ?: "Nome da Empresa"
                } else {
                    Toast.makeText(this, "Empresa não encontrada", Toast.LENGTH_SHORT).show()
                    userNameTextView.text = "Nome da Empresa"
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Erro ao buscar nome da empresa: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
                userNameTextView.text = "Nome da Empresa"
            }
    }

    private fun fetchPedidos(idEmpresa: String) {
        db.collection("pedido")
            .whereEqualTo("idEmpresa", idEmpresa)
            .get()
            .addOnSuccessListener { documents ->
                pedidosList.clear() // Limpa a lista de pedidos para atualizar com os novos dados
                for (document in documents) {
                    // Atribua o ID diretamente ao objeto Pedido
                    val pedido = document.toObject(Pedido::class.java).apply {
                        id = document.id // Aqui o ID é atribuído corretamente ao objeto Pedido
                    }
                    pedidosList.add(pedido) // Adiciona o pedido à lista
                }
                recyclerView.adapter?.notifyDataSetChanged() // Notifica o RecyclerView sobre a mudança de dados
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Erro ao buscar pedidos: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun getLoggedCompanyId(): String {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("id_empresa", "") ?: ""
    }

    class PedidosAdapter(private val pedidos: List<Pedido>, private val context: Context) :
        RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_item_pedido, parent, false)
            return PedidoViewHolder(view)
        }

        override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
            val pedido = pedidos[position] // O pedido já deve ter o ID atribuído aqui
            holder.statusTextView.text = pedido.status
            holder.nomeTextView.text = pedido.nomeProjeto

            // Botão de chat
            holder.chatButton.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, CommentsActivity::class.java)
                intent.putExtra("NOME_PEDIDO", pedido.nomeProjeto)
                intent.putExtra("ID_PEDIDO", pedido.id)
                context.startActivity(intent)
            }

            // Botão de avaliação
            holder.avaliacaoButton.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, AvaliacaoActivity::class.java)
                intent.putExtra("nome_pedido", pedido.nomeProjeto)
                intent.putExtra("id_pedido", pedido.id)
                context.startActivity(intent)
            }
            holder.buttonSendPdfWhatsapp.setOnClickListener {
                onDownloadPDF()
            }
            // Botão para editar o pedido (Exibe um AlertDialog com o formulário)
            holder.editPedidoButton.setOnClickListener {
                showEditDialog(pedido)
            }
        }

        private fun onDownloadPDF() {
            val dadosEmpresa = "Nome: XYZ Ltda\nEndereço: Rua A, 123\nTelefone: (11) 1234-5678"
            val dadosPedido = "Pedido: Projeto X\nDescrição: Desenvolvimento de app\nStatus: Em andamento"
            generatePDF(dadosEmpresa, dadosPedido)
        }

        private fun generatePDF(dadosEmpresa: String, dadosPedido: String) {
            // Cria um documento PDF
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Tamanho A4 em pixels
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            val paint = Paint()
            paint.textSize = 12f

            // Desenha os dados no PDF
            canvas.drawText("Dados da Empresa:", 10f, 30f, paint)
            canvas.drawText(dadosEmpresa, 10f, 50f, paint)
            canvas.drawText("Dados do Pedido:", 10f, 90f, paint)
            canvas.drawText(dadosPedido, 10f, 110f, paint)

            pdfDocument.finishPage(page)

            val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
            val file = File(directoryPath, "Pedido_Detalhado.pdf")

            try {
                pdfDocument.writeTo(FileOutputStream(file))
                // Usando Toast.makeText corretamente
                Toast.makeText(context, "PDF gerado com sucesso em: $directoryPath", Toast.LENGTH_LONG).show()
            } catch (e: IOException) {
                e.printStackTrace()
                // Usando Toast.makeText corretamente
                Toast.makeText(context, "Erro ao gerar PDF: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                pdfDocument.close()
            }
        }

        private fun showEditDialog(pedido: Pedido) {
            // Criação do AlertDialog para editar o pedido
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Editar Pedido")

            // Criação do layout do dialog (um formulário simples)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_pedido, null)

            // Campos de entrada
            val nomeProjetoEditText = view.findViewById<EditText>(R.id.editNomeProjeto)
            val descricaoEditText = view.findViewById<EditText>(R.id.editDescricao)
            val statusSpinner = view.findViewById<Spinner>(R.id.spinnerStatus)
            val clienteSpinner = view.findViewById<Spinner>(R.id.spinnerCliente)

            // Preenchendo os campos com os dados atuais do pedido
            nomeProjetoEditText.setText(pedido.nomeProjeto)
            descricaoEditText.setText(pedido.descricao)

            // Preencher o Spinner de Status com opções
            val statusOptions = arrayOf("Em andamento", "Concluído", "Cancelado")  // Exemplos de status
            val statusAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, statusOptions)
            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            statusSpinner.adapter = statusAdapter

            // Selecionar o status atual do pedido no Spinner
            val statusPosition = statusOptions.indexOf(pedido.status) // Seleciona o item correspondente ao status do pedido
            statusSpinner.setSelection(statusPosition)

            // Preencher o Spinner de Cliente com opções
            val clienteOptions = arrayOf("Cliente A", "Cliente B", "Cliente C")  // Exemplos de clientes
            val clienteAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, clienteOptions)
            clienteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            clienteSpinner.adapter = clienteAdapter

            // Selecionar o cliente atual do pedido no Spinner
            val clientePosition = clienteOptions.indexOf(pedido.cliente) // Seleciona o item correspondente ao cliente do pedido
            clienteSpinner.setSelection(clientePosition)

            builder.setView(view)

            // Botão "Salvar" para aplicar as alterações
            builder.setPositiveButton("Salvar") { dialog, which ->
                val novoNomeProjeto = nomeProjetoEditText.text.toString()
                val novaDescricao = descricaoEditText.text.toString()
                val novoStatus = statusSpinner.selectedItem.toString() // Pegue o valor selecionado no Spinner
                val novoCliente = clienteSpinner.selectedItem.toString() // Pegue o valor selecionado no Spinner

                if (novoNomeProjeto.isNotEmpty() && novaDescricao.isNotEmpty() && novoStatus.isNotEmpty()) {
                    // Atualiza o pedido no Firebase
                    db.collection("pedido").document(pedido.id)
                        .update(
                            "nomeProjeto", novoNomeProjeto,
                            "descricao", novaDescricao,
                            "status", novoStatus,
                            "cliente", novoCliente // Pode ser necessário adaptar se você tiver estrutura diferente
                        )
                        .addOnSuccessListener {
                            Toast.makeText(context, "Pedido atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                            (context as? MenuClienteActivity)?.fetchPedidos(context.getLoggedCompanyId())
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Erro ao atualizar pedido: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            // Botão "Cancelar"
            builder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }

            // Exibe o dialog
            builder.create().show()
        }


        override fun getItemCount(): Int = pedidos.size

        inner class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val statusTextView: TextView = itemView.findViewById(R.id.textViewStatus)
            val nomeTextView: TextView = itemView.findViewById(R.id.textViewNome)
            val chatButton: ImageButton = itemView.findViewById(R.id.buttonChat)
            val avaliacaoButton: ImageButton = itemView.findViewById(R.id.buttonAvaliacao)
            val editPedidoButton: ImageButton = itemView.findViewById(R.id.buttonEditPedido)
            val buttonSendPdfWhatsapp: ImageButton = itemView.findViewById(R.id.buttonSendPdfWhatsapp)
        }
    }
}
