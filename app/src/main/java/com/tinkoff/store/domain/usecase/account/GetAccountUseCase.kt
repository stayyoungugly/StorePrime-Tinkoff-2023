package com.tinkoff.store.domain.usecase.account

import com.tinkoff.store.domain.entity.Account
import com.tinkoff.store.domain.repository.AccountRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetAccountUseCase(
    private val repository: AccountRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Result<Account> {
        return withContext(dispatcher) {
            repository.getAccount()
        }
    }
}
