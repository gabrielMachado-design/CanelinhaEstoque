package com.example.canelinhaestoque.data.repository

import com.example.canelinhaestoque.data.model.Sale
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class SaleRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun saveSale(
        sale: Sale,
        onSucess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
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
            "pagamentos" to sale.payments.map {
                hashMapOf(
                    "tipo" to it.type,
                    "valor" to it.amount,
                    "parcelas" to it.installments,
                    "vencimento" to it.dueDate
                )
            }
        )

        firestore.collection("vendas")
            .add(data)
            .addOnSuccessListener { onSucess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun getSalesByPeriod(startDate: Long, endDate: Long, callback: (List<Sale>) -> Unit) {
        firestore.collection("vendas")
            .whereGreaterThanOrEqualTo("data", startDate)
            .whereLessThanOrEqualTo("data", endDate)
            .addSnapshotListener { result, error ->
                if (error != null || result == null) {
                    callback(emptyList())
                    return@addSnapshotListener
                }

                val salesList = result.toObjects(Sale::class.java)
                callback(salesList)
            }
    }
}