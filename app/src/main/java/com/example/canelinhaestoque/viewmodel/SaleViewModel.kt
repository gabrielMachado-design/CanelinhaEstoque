package com.example.canelinhaestoque.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.canelinhaestoque.data.model.Payment
import com.example.canelinhaestoque.data.model.Product
import com.example.canelinhaestoque.data.model.Sale
import com.example.canelinhaestoque.data.model.SaleItem
import com.example.canelinhaestoque.data.repository.ProductRepository
import com.example.canelinhaestoque.data.repository.SaleRepository

class SaleViewModel: ViewModel() {

    private val saleRepository = SaleRepository()
    private val productRepository = ProductRepository()

    var items by mutableStateOf<List<SaleItem>>(emptyList())
        private set

    var payments by mutableStateOf<List<Payment>>(emptyList())
        private set

    var discount by mutableStateOf(0.0)


    fun addProduct(product: Product) {

        val existing = items.find { it.productId == product.id }

        if (existing != null) {
            items = items.map {
                if (it.productId == product.id) {
                    it.copy(quantity = it.quantity + 1.0)
                } else it
            }
        } else {
            items = items + SaleItem(
                productId = product.id,
                name = product.name,
                quantity = 1.0,
                unitPrice = product.salePrice
            )
        }
    }

    fun removeItem(productId: String) {

        items = items.filter { it.productId != productId }
    }

    fun updatedQuantity(productId: String, quantity: Double) {
        items = items.map {
            if (it.productId == productId) {
                it.copy(quantity = quantity)
            } else it
        }
    }

    fun addPayment(payment: Payment) {
        payments = payments + payment
    }

    fun getTotal(): Double {

        return items.sumOf { (it.unitPrice * it.quantity) - it.itemDiscount } - discount
    }

    fun finalizeSale(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val total = getTotal()
        val sale = Sale(
            items = items,
            payments = payments,
            discount = discount,
            totalAmount = total,
            finalAmount = total
        )
        saleRepository.saveSale(
            sale,
            onSucess = {
                updatedStock()
                clearSale()
                onSuccess()
            },
            onFailure = onFailure
        )

    }
    private fun updatedStock() {
        items.forEach { item ->
        }
    }
    private fun clearSale() {
        items = emptyList()
        payments = emptyList()
        discount = 0.0
    }
}

















    

