package com.tinkoff.store.domain.entity

data class Customer(
    val id: Int,
    val email: String,
    val phoneNumber: String,
    val cardBalance: Int,
    val name: String,
    val surname: String,
    val gender: String,
    val birthdayDate: String,
    val street: String,
    val house: Int,
    val apartment: String?,
    val country: String,
    val city: String
)

