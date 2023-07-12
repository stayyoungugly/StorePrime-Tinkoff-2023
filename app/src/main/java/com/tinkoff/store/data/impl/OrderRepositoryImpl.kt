package com.tinkoff.store.data.impl

import com.tinkoff.store.data.api.Api
import com.tinkoff.store.data.mapper.OrderProductMapper
import com.tinkoff.store.domain.entity.OrderProduct
import com.tinkoff.store.domain.repository.OrderRepository
import com.tinkoff.store.domain.repository.TokenRepository
import timber.log.Timber
import javax.net.ssl.HttpsURLConnection

class OrderRepositoryImpl(
    val api: Api,
    val mapper: OrderProductMapper,
    val tokenRepository: TokenRepository
) : OrderRepository {
    override suspend fun createOrder(ids: ArrayList<Int>): Result<List<OrderProduct>> {
        val response = api.createOrder(ids)
        if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED && !tokenRepository.getToken()
                .isNullOrEmpty()
        ) {
            val result = tokenRepository.refreshToken()
            result.fold(onSuccess = {
                return mapper.mapToResultList(api.createOrder(ids))
            }, onFailure = {
                Timber.e(it)
                return Result.failure(it)
            })
        } else return mapper.mapToResultList(response)
    }
}
