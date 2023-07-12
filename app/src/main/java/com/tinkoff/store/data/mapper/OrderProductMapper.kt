package com.tinkoff.store.data.mapper

import com.google.gson.Gson
import com.tinkoff.store.BuildConfig
import com.tinkoff.store.MyApplication
import com.tinkoff.store.R
import com.tinkoff.store.data.response.OrderProductResponse
import com.tinkoff.store.data.response.ErrorResponse
import com.tinkoff.store.domain.entity.OrderProduct
import com.tinkoff.store.domain.exception.ServerException
import retrofit2.Response

private const val BASE_URL = BuildConfig.SERVER_URL + "/photos/"

class OrderProductMapper {
    fun mapToResult(response: Response<OrderProductResponse>): Result<OrderProduct> {
        return if (response.isSuccessful) {
            val body = requireNotNull(response.body())
            Result.success(
                mapToOrderProduct(body)
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

    fun mapToResultList(response: Response<List<OrderProductResponse>>): Result<List<OrderProduct>> {
        return if (response.isSuccessful) {
            val body = requireNotNull(response.body())
            val list = ArrayList<OrderProduct>()
            for (product in body) {
                list.add(mapToOrderProduct(product))
            }
            Result.success(list)
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

    private fun mapToOrderProduct(response: OrderProductResponse): OrderProduct {
        var status = ""
        when (response.status) {
            "CREATED" -> status = MyApplication.appContext.getString(R.string.status_created)
            "TRANSITING" -> status = MyApplication.appContext.getString(R.string.status_transiting)
            "DELIVERED" -> status = MyApplication.appContext.getString(R.string.status_delivered)
            "CANCELLED" -> status = MyApplication.appContext.getString(R.string.status_cancelled)
        }
        return OrderProduct(
            id = response.id,
            title = response.product.title,
            price = response.product.price.toInt(),
            quantity = response.quantity,
            status = status
        )
    }
}
