package com.tinkoff.store.di

import com.tinkoff.store.BuildConfig
import com.tinkoff.store.data.api.Api
import com.tinkoff.store.data.network.AuthInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = BuildConfig.SERVER_URL
private const val TYPE_HEADER = "Content-Type"
private const val JSON_TYPE = "application/json"

val networkModule = module {

    factory(named("authInterceptor")) {
        AuthInterceptor(get())
    }

    fun provideAuthInterceptor(authInterceptor: AuthInterceptor): Interceptor {
        return authInterceptor
    }

    factory(named("Client")) {
        OkHttpClient.Builder()
            .addInterceptor(typeHeaderInterceptor())
            .addInterceptor(provideAuthInterceptor(get(named("authInterceptor"))))
            .also {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(
                        HttpLoggingInterceptor()
                            .setLevel(
                                HttpLoggingInterceptor.Level.BODY
                            )
                    )
                }
            }
            .build()
    }

    factory<Api> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get(named("Client")))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }
}

private fun typeHeaderInterceptor() = Interceptor { chain ->
    val original = chain.request()
    chain.proceed(
        original.newBuilder()
            .header(
                TYPE_HEADER, JSON_TYPE
            )
            .build()
    )
}
