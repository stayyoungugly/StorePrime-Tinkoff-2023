package com.tinkoff.store.domain.usecase.cart

import com.tinkoff.store.domain.repository.CartRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeleteFromCartUseCase(
    private val repository: CartRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        id: Int,
    ) {
        return withContext(dispatcher) {
            repository.deleteFromCart(
                id
            )
        }
    }
}
