package com.example.canelinhaestoque.data.repository

import com.example.canelinhaestoque.data.model.Sale
import com.google.firebase.firestore.FirebaseFirestore


class SaleRepository {

    private val db = FirebaseFirestore.getInstance()

    fun saveSale(
        sale: Sale, onSucess: () -> Unit,
        onFailure: (Exception) -> Unit
    ){

        val data = hashMapOf(
            "data" to sale.date,
            "forma_pagamento" to sale.paymentMethod,
            "total" to sale.total,
            "desconto_total" to sale.totalDiscount,
            "itens" to sale.items.map {
                hashMapOf(
                    "produto_id" to it.productId,
                    "nome" to it.name,
                    "quantidade" to it.quantity,
                    "preco_unitario" to it.unitPrice,
                    "desconto_item" to it.itemDiscount

                )
            }
        )

        db.collection("vendas")
            .add(data)
            .addOnSuccessListener { onSucess() }
            .addOnFailureListener { onFailure(it) }
    }
}