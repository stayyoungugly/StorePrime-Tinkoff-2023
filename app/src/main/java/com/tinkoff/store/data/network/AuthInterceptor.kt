package com.tinkoff.store.data.network

import com.tinkoff.store.data.database.local.preferences.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response

private const val AUTHORIZATION = "Authorization"

class AuthInterceptor(
    private var preferenceManager: PreferenceManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader(AUTHORIZATION, "Bearer ${preferenceManager.getToken()}")
        return chain.proceed(requestBuilder.build())
    }
}
