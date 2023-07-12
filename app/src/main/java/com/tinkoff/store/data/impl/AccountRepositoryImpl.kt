package com.tinkoff.store.data.impl

import com.tinkoff.store.data.api.Api
import com.tinkoff.store.data.mapper.AccountMapper
import com.tinkoff.store.domain.entity.Account
import com.tinkoff.store.domain.repository.AccountRepository
import com.tinkoff.store.domain.repository.OrderRepository
import com.tinkoff.store.domain.repository.TokenRepository
import timber.log.Timber
import javax.net.ssl.HttpsURLConnection

class AccountRepositoryImpl(
    val api: Api,
    val mapper: AccountMapper,
    val tokenRepository: TokenRepository
) : AccountRepository {
    override suspend fun getAccount(): Result<Account> {
        val response = api.getAccount()
        if (response.code() == HttpsURLConnection.HTTP_UNAUTHORIZED && !tokenRepository.getToken()
                .isNullOrEmpty()
        ) {
            val result = tokenRepository.refreshToken()
            result.fold(onSuccess = {
                return mapper.mapToResult(api.getAccount())
            }, onFailure = {
                Timber.e(it)
                return Result.failure(it)
            })
        } else return mapper.mapToResult(response)
    }
}
