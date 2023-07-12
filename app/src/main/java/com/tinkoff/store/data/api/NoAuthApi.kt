package com.tinkoff.store.data.api

import com.tinkoff.store.data.request.CustomerSignUpRequest
import com.tinkoff.store.data.request.SellerSignUpRequest
import com.tinkoff.store.data.response.*
import retrofit2.Response
import retrofit2.http.*

interface NoAuthApi {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email", encoded = true) email: String,
        @Field("password", encoded = true) password: String
    ): Response<TokenResponse>

    @Headers("Content-Type: application/json")
    @POST("customer")
    suspend fun signUpCustomer(
        @Body registrationRequest: CustomerSignUpRequest
    ): Response<CustomerResponse>

    @Headers("Content-Type: application/json")
    @POST("seller")
    suspend fun signUpSeller(
        @Body registrationRequest: SellerSignUpRequest
    ): Response<SellerResponse>

    @GET("products/random")
    suspend fun getMainProduct(): Response<ProductResponse>

    @GET("products/random/{amount}")
    suspend fun getMainProducts(@Path("amount") amount: Int): Response<List<ProductResponse>>

    @GET("products/search")
    suspend fun searchByQuery(@Query("content") query: String): Response<List<ProductResponse>>

    @GET("products/search")
    suspend fun searchByQueryAndCategory(
        @Query("content") query: String,
        @Query("category") category: String
    ): Response<List<ProductResponse>>

    @GET("category/all")
    suspend fun getCategories(): Response<List<CategoryResponse>>

    @GET("products")
    suspend fun searchByCategory(@Query("category") category: String): Response<List<ProductResponse>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ProductResponse>


}
