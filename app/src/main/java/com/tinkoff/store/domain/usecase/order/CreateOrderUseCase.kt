package com.tinkoff.store.domain.usecase.order

import com.tinkoff.store.domain.entity.CartProduct
import com.tinkoff.store.domain.entity.OrderProduct
import com.tinkoff.store.domain.entity.Product
import com.tinkoff.store.domain.repository.CartRepository
import com.tinkoff.store.domain.repository.OrderRepository
import com.tinkoff.store.domain.repository.ProductsRepository
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
