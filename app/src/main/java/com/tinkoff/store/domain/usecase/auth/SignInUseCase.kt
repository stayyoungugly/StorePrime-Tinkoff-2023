package com.tinkoff.store.domain.usecase.auth

import com.tinkoff.store.domain.entity.Token
import com.tinkoff.store.domain.repository.TokenRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SignInUseCase(
    private val repository: TokenRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<Token> {
        return withContext(dispatcher) {
            repository.getToken(
                email,
                password,
            )
        }
    }
}
