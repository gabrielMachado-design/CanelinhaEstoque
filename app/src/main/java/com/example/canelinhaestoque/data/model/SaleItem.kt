package com.example.canelinhaestoque.data.model

import com.google.firebase.firestore.PropertyName

data class SaleItem(
    val productId: String = "",

    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String = "",

    @get:PropertyName("quantity")
    @set:PropertyName("quantity")
    var quantity: Double = 0.0,

    @get:PropertyName("unitPrice")
    @set:PropertyName("unitPrice")
    var unitPrice: Double = 0.0,

    @get:PropertyName("costPrice")
    @set:PropertyName("costPrice")
    var costPrice: Double = 0.0,

    @get:PropertyName("itemDiscount")
    @set:PropertyName("itemDiscount")
    var itemDiscount: Double = 0.0)

