package com.tinkoff.store.data.mapper

import com.google.gson.Gson
import com.tinkoff.store.data.response.CustomerResponse
import com.tinkoff.store.data.response.ErrorResponse
import com.tinkoff.store.data.response.SellerResponse
import com.tinkoff.store.domain.entity.Customer
import com.tinkoff.store.domain.entity.Seller
import com.tinkoff.store.domain.exception.ServerException
import retrofit2.Response

class SignUpMapper {
    fun mapToCustomerResult(response: Response<CustomerResponse>): Result<Customer> {
        return if (response.isSuccessful) {
            val body = requireNotNull(response.body())
            Result.success(
                Customer(
                    id = body.id,
                    email = body.email,
                    name = body.name,
                    surname = body.surname.toString(),
                    phoneNumber = body.phoneNumber,
                    street = body.addressDto.street,
                    house = body.addressDto.house,
                    apartment = body.addressDto.apartment,
                    country = body.addressDto.location.country,
                    city = body.addressDto.location.city,
                    cardBalance = body.cardBalance,
                    birthdayDate = body.birthdayDate,
                    gender = body.gender
                )
            )
        } else {
            val body = requireNotNull(response.errorBody())
            val errorResponse: ErrorResponse =
                Gson().fromJson(body.string(), ErrorResponse::class.java)
            Result.failure(ServerException(errorResponse.message))
        }
    }

    fun mapToSellerResult(response: Response<SellerResponse>): Result<Seller> {
        return if (response.isSuccessful) {
            val body = requireNotNull(response.body())
            Result.success(
                Seller(
                    id = body.id,
                    email = body.email,
                    name = body.name,
                    phoneNumber = body.phoneNumber,
                    cardBalance = body.cardBalance,
                    country = body.locationDto.country,
                    city = body.locationDto.city,
                    description = body.description
                )
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
}


