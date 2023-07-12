package com.tinkoff.store.domain.usecase.cart

import com.tinkoff.store.domain.entity.CartProduct
import com.tinkoff.store.domain.repository.CartRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AddToCartUseCase(
    private val repository: CartRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        id: Int,
        quantity: Int
    ): Result<CartProduct> {
        return withContext(dispatcher) {
            repository.addToCart(
                id,
                quantity
            )
        }
    }
}
