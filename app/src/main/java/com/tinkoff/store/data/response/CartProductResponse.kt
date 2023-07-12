package com.tinkoff.store.data.response

data class CartProductResponse(
    val customerId: Int,
    val id: Int,
    val product: ProductResponse,
    val quantity: Int
)
