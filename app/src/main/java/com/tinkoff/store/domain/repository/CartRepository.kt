package com.tinkoff.store.domain.repository

import com.tinkoff.store.domain.entity.CartProduct

interface CartRepository {
    suspend fun addToCart(id: Int, quantity: Int): Result<CartProduct>
    suspend fun deleteFromCart(id: Int)
    suspend fun getCartProducts(): Result<List<CartProduct>>
}

