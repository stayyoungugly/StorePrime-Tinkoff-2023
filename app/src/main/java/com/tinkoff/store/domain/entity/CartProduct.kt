package com.tinkoff.store.domain.entity

data class CartProduct(
    val id: Int,
    val amount: Int,
    val imageUrls: ArrayList<String>?,
    val price: Int,
    val title: String,
    val quantity: Int
)
