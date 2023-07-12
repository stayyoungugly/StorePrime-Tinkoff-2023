package com.tinkoff.store.domain.usecase.auth

import com.tinkoff.store.domain.entity.Seller
import com.tinkoff.store.domain.repository.SignUpRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SellerSignUpUseCase(
    private val repository: SignUpRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        description: String,
        email: String,
        password: String,
        inn: String,
        city: String,
        country: String,
        name: String,
        phoneNumber: String
    ): Result<Seller> {
        return withContext(dispatcher) {
            repository.signUpSeller(
                description,
                email,
                password,
                inn,
                city,
                country,
                name,
                phoneNumber
            )
        }
    }
}
