package com.tinkoff.store.domain.usecase.order

import com.tinkoff.store.domain.entity.OrderProduct
import com.tinkoff.store.domain.repository.OrderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CreateOrderUseCase(
    private val repository: OrderRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        ids: ArrayList<Int>
    ): Result<List<OrderProduct>> {
        return withContext(dispatcher) {
            repository.createOrder(ids)
        }
    }
}
