package com.example.canelinhaestoque.data.model

data class Sale(

    val id: String = "",
    val date: Long = System.currentTimeMillis(),
    val payment: List<Payment> = emptyList(),
    val totalAmount: Double = 0.0,
    val finalAmount: Double = 0.0,
    val discount: Double = 0.0,
    val items: List<SaleItem> = emptyList(),
    val payments: List<Payment>
)
