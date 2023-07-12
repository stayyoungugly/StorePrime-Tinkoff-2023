package com.tinkoff.store.data.request

data class SellerSignUpRequest(
    val description: String,
    val email: String,
    val inn: String,
    val locationDto: LocationForm,
    val name: String,
    val phoneNumber: String,
    val passwordHash: String
)
