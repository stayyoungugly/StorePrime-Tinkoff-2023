package com.tinkoff.store.data.api

import com.tinkoff.store.data.response.*
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @POST("auth/login")
    suspend fun refreshToken(): Response<TokenResponse>

    @POST("auth/token/revoke")
    suspend fun tokenRevoke()

    @POST("carts/{productId}")
    suspend fun addToCart(
        @Path("productId") id: Int,
        @Query("quantity") quantity: Int
    ): Response<CartProductResponse>

    @DELETE("carts/{productId}")
    suspend fun deleteFromCart(@Path("productId") id: Int): Response<ErrorResponse>

    @GET("carts")
    suspend fun getCartProducts(): Response<List<CartProductResponse>>

    @GET("accounts")
    suspend fun getAccount(): Response<CustomerResponse>

    @POST("orders")
    suspend fun createOrder(@Body ids: ArrayList<Int>): Response<List<OrderProductResponse>>
}
