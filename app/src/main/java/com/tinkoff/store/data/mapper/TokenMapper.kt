package com.tinkoff.store.data.mapper

import com.google.gson.Gson
import com.tinkoff.store.data.response.ErrorResponse
import com.tinkoff.store.data.response.SellerResponse
import com.tinkoff.store.data.response.TokenResponse
import com.tinkoff.store.domain.entity.Seller
import com.tinkoff.store.domain.entity.Token
import com.tinkoff.store.domain.exception.ServerException
import retrofit2.Response
import timber.log.Timber

class TokenMapper {
    fun mapToResult(response: Response<TokenResponse>): Result<Token> {
        return if (response.isSuccessful) {
            val body = requireNotNull(response.body())
            Result.success(
                Token(
                    accessToken = body.accessToken,
                    refreshToken = body.refreshToken
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


