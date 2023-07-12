package com.tinkoff.store.data.request

data class CustomerSignUpRequest(
    val addressDto: AddressForm,
    val birthdayDate: String,
    val email: String,
    val gender: String,
    val name: String,
    val passwordHash: String,
    val phoneNumber: String,
    val surname: String
)
