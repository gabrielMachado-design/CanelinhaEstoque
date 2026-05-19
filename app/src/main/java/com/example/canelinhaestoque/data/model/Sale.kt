package com.example.canelinhaestoque.data.model

import com.google.firebase.firestore.PropertyName


data class Sale(
    val id: String = "",

    @get:PropertyName("date")
    @set:PropertyName("date")
    var date: Long = System.currentTimeMillis(),

    @get:PropertyName("totalAmount")
    @set:PropertyName("totalAmount")
    var totalAmount: Double = 0.0,

    @get:PropertyName("items")
    @set:PropertyName("items")
    var items: List<SaleItem> = emptyList(),

    @get:PropertyName("discount")
    @set:PropertyName("discount")
    var discount: Double = 0.0,

    @get:PropertyName("payments")
    @set:PropertyName("payments")
    var payments: List<Payment> = emptyList(),

    @get:PropertyName("finalAmount")
    @set:PropertyName("finalAmount")
    var finalAmount: Double = 0.0
)

