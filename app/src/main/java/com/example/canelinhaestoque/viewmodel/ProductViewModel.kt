package com.example.canelinhaestoque.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.canelinhaestoque.data.model.Product
import com.example.canelinhaestoque.data.repository.ProductRepository
import android.content.Context

class ProductViewModel : ViewModel() {



    private val repository = ProductRepository()

    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products: State<List<Product>> = _products

    fun loadProducts() {
        repository.getProducts {
            _products.value = it.reversed()
        }
    }
    fun deleteProduct(productId: String) {
        repository.deleteProduct(productId,
            onSucess = {
                loadProducts()
            },
            onFailure = { "Falha ao excluir" }
        )
    }

    fun generatePdfReport(context: Context) {
        // repository é a variável que você usa para acessar o ProductRepository
        // products.value é a lista atual de produtos que está na tela
        repository.generatePdfReport(context, products.value)
    }
}