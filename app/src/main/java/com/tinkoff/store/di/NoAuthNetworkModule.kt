package com.tinkoff.store.di

import com.tinkoff.store.BuildConfig
import com.tinkoff.store.data.api.NoAuthApi
import com.tinkoff.store.data.impl.TokenRepositoryImpl
import com.tinkoff.store.domain.repository.TokenRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = BuildConfig.SERVER_URL
val noAuthModule = module {

    factory(named("ClientNoAuth")) {
        OkHttpClient.Builder()
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

    factory<NoAuthApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get(named("ClientNoAuth")))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoAuthApi::class.java)
    }

//    factory<TokenRepository> {
//        (TokenRepositoryImpl(
//            get(named("LoginApiRetrofit")),
//            get(),
//            get(),
//            get()
//        ))
//    }

}
