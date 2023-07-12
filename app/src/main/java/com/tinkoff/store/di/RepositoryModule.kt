package com.tinkoff.store.di

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.tinkoff.store.data.database.local.preferences.PreferenceManager
import com.tinkoff.store.data.impl.*
import com.tinkoff.store.data.mapper.*
import com.tinkoff.store.domain.repository.*
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

const val FILE_NAME = "SharedFileCrypted"

val repositoryModule = module {
    factoryOf(::SignUpRepositoryImpl) { bind<SignUpRepository>() }
    factoryOf(::TokenRepositoryImpl) { bind<TokenRepository>() }
    factoryOf(::ProductsRepositoryImpl) { bind<ProductsRepository>() }
    factoryOf(::CartRepositoryImpl) { bind<CartRepository>() }
    factoryOf(::CategoriesRepositoryImpl) { bind<CategoriesRepository>() }
    factoryOf(::OrderRepositoryImpl) { bind<OrderRepository>() }
    factoryOf(::AccountRepositoryImpl) { bind<AccountRepository>() }

    factoryOf(::PreferenceManager)

    factoryOf(::SignUpMapper)
    factoryOf(::CartProductMapper)
    factoryOf(::ProductMapper)
    factoryOf(::TokenMapper)
    factoryOf(::CategoryMapper)
    factoryOf(::OrderProductMapper)
    factoryOf(::AccountMapper)
}

val sharedPreferencesModule = module {
    single { provideSharedPref(androidApplication()) }
}

fun provideSharedPref(app: Application): SharedPreferences {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    return EncryptedSharedPreferences.create(
        FILE_NAME,
        masterKeyAlias,
        app.applicationContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}
