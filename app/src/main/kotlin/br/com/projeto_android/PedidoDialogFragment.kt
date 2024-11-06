package br.com.projeto_android

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import br.com.projeto_android.MainActivity.Companion.db
import models.Pedido

class PedidoDialogFragment(private val pedido: Pedido) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = android.app.AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_pedido, null)

        val editNomeProjeto: EditText = view.findViewById(R.id.editNomeProjeto)
        val editDescricao: EditText = view.findViewById(R.id.editDescricao)
        val spinnerStatus: Spinner = view.findViewById(R.id.spinnerStatus)
        val spinnerCliente: Spinner = view.findViewById(R.id.spinnerCliente)
        val btnSalvarPedido: Button = view.findViewById(R.id.btnSalvarPedido)

        editNomeProjeto.setText(pedido.nomeProjeto)
        editDescricao.setText(pedido.descricao)

        val statusList = listOf("Pendente", "Em Andamento", "Concluído") // Exemplo de status
        val statusAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statusList)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = statusAdapter

        // Buscar o ID da empresa nas SharedPreferences
        val idEmpresa = getLoggedCompanyId()

        if (idEmpresa.isNotEmpty()) {
            // Buscar clientes da empresa no Firestore
            fetchClientes(idEmpresa, spinnerCliente)
        } else {
            Toast.makeText(requireContext(), "ID da empresa não encontrado", Toast.LENGTH_SHORT).show()
        }

        btnSalvarPedido.setOnClickListener {
            val nomeProjeto = editNomeProjeto.text.toString()
            val descricao = editDescricao.text.toString()
            val status = spinnerStatus.selectedItem.toString()
            val cliente = spinnerCliente.selectedItem.toString()

            if (nomeProjeto.isNotEmpty() && descricao.isNotEmpty()) {
                val pedidoAtualizado = Pedido(
                    id = pedido.id, // Usando o ID do pedido
                    nomeProjeto = nomeProjeto,
                    descricao = descricao,
                    status = status,
                    cliente = cliente
                )

                if (isConnectedToInternet()) {
                    try {
                        db.collection("pedido")
                            .document(pedido.id)
                            .set(pedidoAtualizado)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Pedido atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                                dismiss()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(requireContext(), "Erro ao atualizar pedido: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Erro inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(requireContext(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }


        builder.setView(view)
        return builder.create()
    }

    private fun isConnectedToInternet(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    // Função para obter o ID da empresa das SharedPreferences
    private fun getLoggedCompanyId(): String {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString("id_empresa", "") ?: ""
    }

    private fun fetchClientes(idEmpresa: String, spinner: Spinner) {
        db.collection("cliente")
            .whereEqualTo("idEmpresa", idEmpresa) // Filtra clientes pela empresa
            .get()
            .addOnSuccessListener { documents ->
                val clientes = mutableListOf<String>()
                val clientesIds = mutableListOf<String>() // Lista para armazenar os IDs dos clientes

                for (document in documents) {
                    val nomeCliente = document.getString("nome") // Assume que o nome do cliente está no campo "nome"
                    val idCliente = document.id // O ID do documento no Firestore

                    if (nomeCliente != null) {
                        clientes.add(nomeCliente)
                        clientesIds.add(idCliente) // Adiciona o ID do cliente
                    }
                }

                // Se houver clientes, preenche o Spinner
                if (clientes.isNotEmpty()) {
                    val clienteAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, clientes)
                    clienteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = clienteAdapter

                    // Guarda os IDs correspondentes aos nomes dos clientes
                    spinner.tag = clientesIds // Usa o `tag` para armazenar os IDs no Spinner
                } else {
                    Toast.makeText(requireContext(), "Nenhum cliente encontrado para esta empresa", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Erro ao buscar clientes: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
