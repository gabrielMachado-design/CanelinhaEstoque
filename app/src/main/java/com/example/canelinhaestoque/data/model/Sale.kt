package com.example.canelinhaestoque.data.model

data class Sale(

    val id: String = "",
    val date: Long = System.currentTimeMillis(),
    val paymentMethod: String = "",
    val total: Double = 0.0,
    val totalDiscount: Double = 0.0,
    val items: List<SaleItem> = emptyList()
)
