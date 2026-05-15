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
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(
    private val saleRepository: SaleRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    var items by mutableStateOf<List<SaleItem>>(emptyList())
        private set

    var allProducts by mutableStateOf<List<Product>>(emptyList())
        private set

    var payments by mutableStateOf<List<Payment>>(emptyList())
        private set

    var discount by mutableStateOf(0.0)

    fun loadInventory() {
        productRepository.getProducts { products ->
            allProducts = products
        }
    }


    fun updateQuantity(productId: String, newQuantity: Double) {
        if (newQuantity <= 0) {
            removeItem(productId)
            return
        }
        items = items.map {
            if (it.productId == productId) it.copy(quantity = newQuantity) else it
        }
    }

    fun addProduct(product: Product) {
        val existing = items.find { it.productId == product.id }
        if (existing != null) {
            items = items.map {
                if (it.productId == product.id) it.copy(quantity = it.quantity + 1.0) else it
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

    fun getTotal(): Double {
        return items.sumOf { (it.unitPrice * it.quantity) } - discount
    }

    fun finalizeSale(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val total = getTotal()
        val sale = Sale(
            items = items,
            payments = payments,
            totalAmount = total,
            finalAmount = total,
            discount = discount
        )

        saleRepository.saveSale(
            sale = sale,
            onSucess = {
                items.forEach { item ->
                    productRepository.updateProductStock(item.productId, item.quantity)
                }
                clearSale()
                onSuccess()
            },
            onFailure = onFailure
        )
    }

    private fun clearSale() {
        items = emptyList()
        payments = emptyList()
        discount = 0.0
    }
}