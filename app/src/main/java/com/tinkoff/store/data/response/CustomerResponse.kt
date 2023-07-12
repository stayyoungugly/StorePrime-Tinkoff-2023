package com.tinkoff.store.data.response

import com.tinkoff.store.data.request.AddressForm

data class CustomerResponse(
    val id: Int,
    val email: String,
    val phoneNumber: String,
    val cardBalance: Int,
    val name: String,
    val surname: String?,
    val gender: String,
    val birthdayDate: String,
    val addressDto: AddressForm
)
