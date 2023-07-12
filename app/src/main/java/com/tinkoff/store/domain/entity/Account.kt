package com.tinkoff.store.domain.entity

data class Account(
    val id: Int,
    val cardBalance: Int,
    val name: String,
    val surname: String?,
)
