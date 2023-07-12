package com.tinkoff.store.domain.repository

import com.tinkoff.store.domain.entity.Account
import com.tinkoff.store.domain.entity.OrderProduct

interface AccountRepository {
    suspend fun getAccount(): Result<Account>
}

