package com.tinkoff.store.data.impl

import com.tinkoff.store.data.api.NoAuthApi
import com.tinkoff.store.data.mapper.SignUpMapper
import com.tinkoff.store.data.request.AddressForm
import com.tinkoff.store.data.request.CustomerSignUpRequest
import com.tinkoff.store.data.request.LocationForm
import com.tinkoff.store.data.request.SellerSignUpRequest
import com.tinkoff.store.domain.entity.Customer
import com.tinkoff.store.domain.entity.Seller
import com.tinkoff.store.domain.repository.SignUpRepository

class SignUpRepositoryImpl(
    private val api: NoAuthApi,
    private val registerMapper: SignUpMapper,
) : SignUpRepository {
    override suspend fun signUpCustomer(
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
    ): Result<Customer> {
        return registerMapper.mapToCustomerResult(
            api.signUpCustomer(
                createCustomerSignUpRequest(
                    birthdayDate,
                    email,
                    gender,
                    name,
                    password,
                    phoneNumber,
                    surname,
                    apartment,
                    house,
                    street,
                    city,
                    country
                )
            )
        )
    }

    override suspend fun signUpSeller(
        description: String,
        email: String,
        password: String,
        inn: String,
        city: String,
        country: String,
        name: String,
        phoneNumber: String,
    ): Result<Seller> {
        return registerMapper.mapToSellerResult(
            api.signUpSeller(
                createSellerSignUpRequest(
                    description,
                    email,
                    password,
                    inn,
                    city,
                    country,
                    name,
                    phoneNumber
                )
            )
        )
    }

    private fun createCustomerSignUpRequest(
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
    ): CustomerSignUpRequest =
        CustomerSignUpRequest(
            email = email,
            birthdayDate = birthdayDate,
            gender = gender,
            name = name,
            passwordHash = password,
            phoneNumber = phoneNumber,
            surname = surname,
            addressDto = AddressForm(
                apartment = apartment,
                house = house,
                street = street,
                location = LocationForm(
                    city = city,
                    country = country
                )
            )
        )

    private fun createSellerSignUpRequest(
        description: String,
        email: String,
        password: String,
        inn: String,
        city: String,
        country: String,
        name: String,
        phoneNumber: String
    ): SellerSignUpRequest =
        SellerSignUpRequest(
            email = email,
            description = description,
            passwordHash = password,
            inn = inn,
            phoneNumber = phoneNumber,
            name = name,
            locationDto = LocationForm(
                city = city,
                country = country
            )
        )
}
