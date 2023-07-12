package com.tinkoff.store.domain.usecase.cart

import com.tinkoff.store.domain.entity.CartProduct
import com.tinkoff.store.domain.repository.CartRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetCartProductsUseCase(
    private val repository: CartRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
    ): Result<List<CartProduct>> {
        return withContext(dispatcher) {
            repository.getCartProducts()
        }
    }
}
