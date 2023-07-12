package com.tinkoff.store.data.response

data class OrderProductResponse(
    val customerId: Int,
    val id: Int,
    val status: String,
    val product: ProductResponse,
    val quantity: Int
)
