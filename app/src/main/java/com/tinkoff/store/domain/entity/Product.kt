package com.tinkoff.store.domain.entity

data class Product(
    val id: Int,
    val amount: Int,
    val category: String?,
    val description: String,
    val imageUrls: ArrayList<String>?,
    val price: Int,
    val sellerId: Int,
    val title: String,
    val sellerCountry: String,
    val sellerCity: String,
    val sellerName: String,
)
