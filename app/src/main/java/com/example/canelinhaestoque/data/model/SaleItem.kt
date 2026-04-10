package com.example.canelinhaestoque.data.model

data class SaleItem(
    val productId: String = "",
    val name: String = "",
    val quantity: Double = 0.0,
    val unitPrice: Double = 0.0,
    val itemDiscount: Double = 0.0
)
