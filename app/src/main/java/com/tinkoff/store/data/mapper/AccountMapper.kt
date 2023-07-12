package com.tinkoff.store.data.mapper

import com.google.gson.Gson
import com.tinkoff.store.MyApplication
import com.tinkoff.store.R
import com.tinkoff.store.data.response.CustomerResponse
import com.tinkoff.store.data.response.ErrorResponse
import com.tinkoff.store.data.response.ProductResponse
import com.tinkoff.store.domain.entity.Account
import com.tinkoff.store.domain.entity.Product
import com.tinkoff.store.domain.exception.ServerException
import retrofit2.Response

class AccountMapper {
    fun mapToResult(response: Response<CustomerResponse>): Result<Account> {
        return if (response.isSuccessful) {
            val body = requireNotNull(response.body())
            Result.success(
                mapToAccount(body)
            )
        } else {
            val message = response.message()
            val body = requireNotNull(response.errorBody())
            try {
                val errorResponse: ErrorResponse =
                    Gson().fromJson(body.string(), ErrorResponse::class.java)
                Result.failure(ServerException(errorResponse.serviceMessage))
            } catch (ex: Exception) {
                Result.failure(ServerException(message))
            }
        }
    }

    private fun mapToAccount(response: CustomerResponse): Account {
        return Account(
            id = response.id,
            name = response.name,
            surname = response.surname,
            cardBalance = response.cardBalance
        )
    }

}
