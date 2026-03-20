package com.example.canelinhaestoque.data.repository

import com.example.canelinhaestoque.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class ProductRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getProducts(callback:(List<Product>) -> Unit){  db.collection("produtos")
        .get()
        .addOnSuccessListener {
                result ->

            val productList = mutableListOf<Product>()
            for (document in result)
            {

                val product = Product(id = document.id, name = document.getString("nome") ?: "",
                    description = document.getString("descrição") ?: "",
                    costPrice = document.getDouble("preco_custo") ?: 0.0,
                    salePrice = document.getDouble("preco_venda") ?: 0.0,
                    stockQuantity = document.getLong("estoque")?.toInt() ?: 0,
                    photoUrl = document.getString("foto_url") ?: "")

                productList.add(product) }
            callback(productList)
        }
        .addOnFailureListener {
            callback(emptyList())

        }

    }

    fun addProduct(product: Product, onSucess: () -> Unit, onFailure: (Exception) -> Unit) {

        val data = hashMapOf(

            "nome" to product.name,
            "descrição" to product.description,
            "preco_custo" to product.costPrice,
            "preco_venda" to product.salePrice,
            "estoque" to product.stockQuantity,
            "foto_url" to product.photoUrl
        )

        db.collection("produtos")
            .add(data)
            .addOnSuccessListener { onSucess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }
    fun updateProduct(product: Product, onSucess: () -> Unit, onFailure: (Exception) -> Unit){

        val data = hashMapOf(

            "nome" to product.name,
            "descrição" to product.description,
            "preco_custo" to product.costPrice,
            "preco_venda" to product.salePrice,
            "estoque" to product.stockQuantity,
            "foto_url" to product.photoUrl
        )
        db.collection("produtos")
            .document(product.id)
            .update(data as Map<String, Any>)
            .addOnSuccessListener { onSucess() }
            .addOnFailureListener{ exception -> onFailure(exception)}

    }

    fun deleteProduct(productId: String, onSucess: () -> Unit, onFailure: (Exception) -> Unit) {

        db.collection("produtos")
            .document(productId)
            .delete()
            .addOnSuccessListener { onSucess() }
            .addOnFailureListener { exception -> onFailure(exception) }

    }

}





