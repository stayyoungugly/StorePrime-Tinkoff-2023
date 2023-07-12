package com.tinkoff.store.data.impl

import com.tinkoff.store.data.api.Api
import com.tinkoff.store.data.api.NoAuthApi
import com.tinkoff.store.data.database.local.preferences.PreferenceManager
import com.tinkoff.store.data.mapper.TokenMapper
import com.tinkoff.store.domain.entity.Token
import com.tinkoff.store.domain.repository.TokenRepository
import timber.log.Timber

class TokenRepositoryImpl(
    private val noAuthApi: NoAuthApi,
    private val api: Api,
    private var preferenceManager: PreferenceManager,
    private val tokenMapper: TokenMapper
) : TokenRepository {
    override suspend fun getToken(email: String, password: String): Result<Token> {
        val result = tokenMapper.mapToResult(noAuthApi.login(email, password))
        result.fold(onSuccess = {
            preferenceManager.storeToken(it.accessToken)
            preferenceManager.storeRefreshToken(it.refreshToken)
        }, onFailure = {
            Timber.e(it)
        })
        return result
    }

    override suspend fun refreshToken(): Result<Token> {
        val token = preferenceManager.getRefreshToken()
        if (token != null) {
            preferenceManager.storeToken(token)
        }
        val response = api.refreshToken()
        val result =
            tokenMapper.mapToResult(response)
        result.fold(onSuccess = {
            preferenceManager.storeToken(it.accessToken)
            preferenceManager.storeRefreshToken(it.refreshToken)
        }, onFailure = {
            Timber.e(it)
            preferenceManager.deleteToken()
        })
        return result
    }

    override suspend fun deleteToken() {
        api.tokenRevoke()
        preferenceManager.deleteToken()
    }

    override fun getToken(): String? {
        return preferenceManager.getToken()
    }

    override fun getRefreshToken(): String? {
        return preferenceManager.getRefreshToken()
    }

    fun saveToken(token: String) {
        preferenceManager.storeToken(token)
    }

    fun saveRefreshToken(refreshToken: String) {
        preferenceManager.storeToken(refreshToken)
    }

}
