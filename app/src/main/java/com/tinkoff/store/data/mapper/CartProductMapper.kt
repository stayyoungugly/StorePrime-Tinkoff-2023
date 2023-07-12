package com.tinkoff.store.data.mapper

import com.google.gson.Gson
import com.tinkoff.store.BuildConfig
import com.tinkoff.store.data.response.CartProductResponse
import com.tinkoff.store.data.response.ErrorResponse
import com.tinkoff.store.data.response.ProductResponse
import com.tinkoff.store.domain.entity.CartProduct
import com.tinkoff.store.domain.entity.Product
import com.tinkoff.store.domain.exception.ServerException
import retrofit2.Response

private const val BASE_URL = BuildConfig.SERVER_URL + "/photos/"

class CartProductMapper {
    fun mapToResult(response: Response<CartProductResponse>): Result<CartProduct> {
        return if (response.isSuccessful) {
            val body = requireNotNull(response.body())
            Result.success(
                mapToCartProduct(body)
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

    fun mapToResultList(response: Response<List<CartProductResponse>>): Result<List<CartProduct>> {
        return if (response.isSuccessful) {
            val body = requireNotNull(response.body())
            val list = ArrayList<CartProduct>()
            for (product in body) {
                list.add(mapToCartProduct(product))
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

    private fun mapToCartProduct(response: CartProductResponse): CartProduct {
        val ids = response.product.imageIds
        val urls = ArrayList<String>()
        for (id in ids) {
            urls.add(generateUrl(id))
        }
        return CartProduct(
            id = response.id,
            imageUrls = urls,
            title = response.product.title,
            amount = response.product.amount,
            price = response.product.price.toInt(),
            quantity = response.quantity
        )
    }

    private fun generateUrl(id: String): String {
        return BASE_URL + id
    }
}
