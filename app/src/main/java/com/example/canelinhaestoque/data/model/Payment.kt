package com.example.canelinhaestoque.data.model

data class Payment(
    val type: String = "",
    val amount: Double = 0.0,
    val dueDate: String = ""
)