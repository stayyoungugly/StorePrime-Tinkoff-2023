package com.tinkoff.store.domain.repository

import com.tinkoff.store.domain.entity.Token

interface TokenRepository {
    suspend fun getToken(email: String, password: String): Result<Token>
    fun getToken(): String?
    fun getRefreshToken(): String?
    suspend fun refreshToken(): Result<Token>
    suspend fun deleteToken()
}
