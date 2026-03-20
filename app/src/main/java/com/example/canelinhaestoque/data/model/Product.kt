package com.example.canelinhaestoque.data.model

data class Product(

    val id: String = "",
    val name: String = "",
    val description: String = "",
    val costPrice: Double = 0.0,
    val salePrice: Double = 0.0,
    val stockQuantity: Int = 0,
    val photoUrl: String = ""

)