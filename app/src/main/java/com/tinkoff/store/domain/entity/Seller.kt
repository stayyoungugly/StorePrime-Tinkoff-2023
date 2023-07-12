package com.tinkoff.store.domain.entity

data class Seller(
    val id: Int,
    val email: String,
    val phoneNumber: String,
    val cardBalance: Int,
    val name: String,
    val description: String,
    val country: String,
    val city: String
)

