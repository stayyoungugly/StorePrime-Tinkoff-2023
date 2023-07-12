package com.tinkoff.store.domain.repository

import com.tinkoff.store.domain.entity.OrderProduct

interface OrderRepository {
    suspend fun createOrder(ids: ArrayList<Int>): Result<List<OrderProduct>>
}

