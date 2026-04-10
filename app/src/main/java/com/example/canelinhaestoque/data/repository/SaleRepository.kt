package com.example.canelinhaestoque.data.repository

import com.example.canelinhaestoque.data.model.Sale
import com.google.firebase.firestore.FirebaseFirestore


class SaleRepository {

    private val db = FirebaseFirestore.getInstance()

    fun saveSale(
        sale: Sale,
        onSucess: () -> Unit,
        onFailure: (Exception) -> Unit
    ){
        val data = hashMapOf(
            "data" to sale.date,
            "total" to sale.totalAmount,
            "desconto_total" to sale.discount,
            "itens" to sale.items.map {
                hashMapOf(
                    "produto_id" to it.productId,
                    "nome" to it.name,
                    "quantidade" to it.quantity,
                    "preco_unitario" to it.unitPrice,
                    "desconto_item" to it.itemDiscount

                )
            },
            "pagamentos" to sale.payment.map {
                hashMapOf(
                    "tipo" to it.type,
                    "valor" to it.amount,
                    "parcelas" to it.installments,
                    "vencimento" to it.dueDate
                )
            }
        )

        db.collection("vendas")
            .add(data)
            .addOnSuccessListener { onSucess() }
            .addOnFailureListener { onFailure(it) }
    }
}