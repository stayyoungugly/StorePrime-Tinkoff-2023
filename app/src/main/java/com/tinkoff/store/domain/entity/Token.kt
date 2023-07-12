package com.tinkoff.store.domain.entity

data class Token(
    val accessToken: String,
    val refreshToken: String
)
