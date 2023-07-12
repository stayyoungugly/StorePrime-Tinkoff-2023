package com.tinkoff.store.domain.entity

data class OrderProduct(
    val id: Int,
    val status: String,
    val price: Int,
    val title: String,
    val quantity: Int
)
