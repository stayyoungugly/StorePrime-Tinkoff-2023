package com.tinkoff.store.domain.usecase.auth

import com.tinkoff.store.domain.entity.Customer
import com.tinkoff.store.domain.repository.SignUpRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CustomerSignUpUseCase(
    private val repository: SignUpRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
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
        return withContext(dispatcher) {
            repository.signUpCustomer(
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
        }
    }
}
