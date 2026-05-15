package com.example.canelinhaestoque.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.canelinhaestoque.data.model.Product
import com.example.canelinhaestoque.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {



    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products: State<List<Product>> = _products


    init {
        loadProducts()
    }

    fun loadProducts() {
        repository.getProducts { list ->
            _products.value = list.reversed()
        }
    }

    fun deleteProduct(productId: String) {
        repository.deleteProduct(
            productId = productId,
            onSucess = {

                loadProducts()
            },
            onFailure = {

            }
        )
    }

    fun generatePdfReport(context: Context) {

        repository.generatePdfReport(context, products.value)
    }
}