package com.tinkoff.store.domain.repository

import com.tinkoff.store.domain.entity.Account

interface AccountRepository {
    suspend fun getAccount(): Result<Account>
}

