package com.tinkoff.store.domain.usecase.token

import com.tinkoff.store.domain.repository.TokenRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeleteTokenUseCase(
    private val repository: TokenRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
    ) {
        return withContext(dispatcher) {
            repository.deleteToken()
        }
    }
}
