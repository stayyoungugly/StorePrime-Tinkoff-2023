package com.tinkoff.store.domain.repository

import com.tinkoff.store.domain.entity.Customer
import com.tinkoff.store.domain.entity.Seller

interface SignUpRepository {

    suspend fun signUpCustomer(
        birthdayDate: String,
        email: String,
        gender: String,
        name: String,
        password: String,
        phoneNumber: String,
        surname: String,
        apartment: String?,
        house: Int,
        street: String,
        city: String,
        country: String
    ): Result<Customer>

    suspend fun signUpSeller(
        description: String,
        email: String,
        password: String,
        inn: String,
        city: String,
        country: String,
        name: String,
        phoneNumber: String
    ): Result<Seller>
}
