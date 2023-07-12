package com.tinkoff.store.di

import kotlinx.coroutines.Dispatchers
import com.tinkoff.store.domain.usecase.auth.*
import com.tinkoff.store.domain.usecase.products.*
import com.tinkoff.store.domain.usecase.category.*
import com.tinkoff.store.domain.usecase.cart.*
import com.tinkoff.store.domain.usecase.order.*
import com.tinkoff.store.domain.usecase.account.*
import com.tinkoff.store.domain.usecase.token.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {

    factoryOf(::CustomerSignUpUseCase)
    factoryOf(::SellerSignUpUseCase)
    factoryOf(::SignInUseCase)

    factoryOf(::MainProductsUseCase)
    factoryOf(::MainProductUseCase)
    factoryOf(::SearchByQueryUseCase)
    factoryOf(::SearchByCategoryUseCase)
    factoryOf(::SearchByQueryAndCategoryUseCase)
    factoryOf(::GetProductByIdUseCase)

    factoryOf(::GetCategoriesUseCase)

    factoryOf(::AddToCartUseCase)
    factoryOf(::DeleteFromCartUseCase)
    factoryOf(::GetCartProductsUseCase)

    factoryOf(::CreateOrderUseCase)

    factoryOf(::GetAccountUseCase)

    factoryOf(::DeleteTokenUseCase)

    factory { Dispatchers.Default }
}
