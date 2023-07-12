package com.tinkoff.store.data.impl

import com.tinkoff.store.data.api.Api
import com.tinkoff.store.data.mapper.CartProductMapper
import com.tinkoff.store.domain.entity.CartProduct
import com.tinkoff.store.domain.exception.ServerException
import com.tinkoff.store.domain.repository.CartRepository
import com.tinkoff.store.domain.repository.TokenRepository
import timber.log.Timber
import javax.net.ssl.HttpsURLConnection

class CartRepositoryImpl(
    val api: Api,
    val mapper: CartProductMapper,
    val tokenRepository: TokenRepository
) : CartRepository {
    override suspend fun addToCart(id: Int, quantity: Int): Result<CartProduct> {
        val response = api.addToCart(id, quantity)
        if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED && !tokenRepository.getToken()
                .isNullOrEmpty()
        ) {
            val result = tokenRepository.refreshToken()
            result.fold(onSuccess = {
                return mapper.mapToResult(api.addToCart(id, quantity))
            }, onFailure = {
                Timber.e(it)
                return Result.failure(it)
            })
        } else return mapper.mapToResult(response)
    }

    override suspend fun deleteFromCart(id: Int) {
        val response = api.deleteFromCart(id)
        if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED && !tokenRepository.getToken()
                .isNullOrEmpty()
        ) {
            val result = tokenRepository.refreshToken()
            result.fold(onSuccess = {
                api.deleteFromCart(id)
            }, onFailure = {
                Timber.e(it)
                throw ServerException(requireNotNull(response.body()).serviceMessage)
            })
        }
    }

    override suspend fun getCartProducts(): Result<List<CartProduct>> {
        val response = api.getCartProducts()
        if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED && !tokenRepository.getToken()
                .isNullOrEmpty()
        ) {
            val result = tokenRepository.refreshToken()
            result.fold(onSuccess = {
                return mapper.mapToResultList(api.getCartProducts())
            }, onFailure = {
                Timber.e(it)
                return Result.failure(it)
            })
        } else return mapper.mapToResultList(response)
    }
}
