package service

import com.google.firebase.firestore.FirebaseFirestore

class FirestoreCRUD<T : Any>(
    private val collectionName: String,
    private val clazz: Class<T>,
    private val getCompanyId: () -> String // Função para obter o id_empresa da empresa logada
) {
    private val db = FirebaseFirestore.getInstance()

    // Create
    fun add(item: T, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val itemWithCompanyId = item as? MutableMap<String, Any> ?: item.toMap()
        itemWithCompanyId["id_empresa"] = getCompanyId() // Adiciona o ID da empresa ao item

        db.collection(collectionName)
            .add(itemWithCompanyId)
            .addOnSuccessListener { documentReference ->
                onSuccess(documentReference.id)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    // Read all by company ID
    fun getAll(onSuccess: (List<T>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(collectionName)
            .whereEqualTo("id_empresa", getCompanyId()) // Filtra pelo ID da empresa
            .get()
            .addOnSuccessListener { result ->
                val items = result.mapNotNull { it.toObject(clazz) }
                onSuccess(items)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    // Read by ID
    fun getById(id: String, onSuccess: (T?) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(collectionName).document(id)
            .get()
            .addOnSuccessListener { document ->
                val item = document.toObject(clazz)
                onSuccess(item)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    // Update
    fun update(id: String, item: T, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val itemWithCompanyId = item as? MutableMap<String, Any> ?: item.toMap()
        itemWithCompanyId["id_empresa"] = getCompanyId() // Garante que o ID da empresa seja mantido

        db.collection(collectionName).document(id)
            .set(itemWithCompanyId)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    // Delete
    fun delete(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(collectionName).document(id)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    // Helper function to convert data class to Map if needed
    private fun T.toMap(): MutableMap<String, Any> {
        return this::class.java.declaredFields.associate {
            it.isAccessible = true
            it.name to (it.get(this) ?: "")
        }.toMutableMap()
    }

}
