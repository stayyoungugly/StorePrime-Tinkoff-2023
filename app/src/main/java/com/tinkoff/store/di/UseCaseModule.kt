package com.tinkoff.store.di

import com.tinkoff.store.domain.usecase.account.GetAccountUseCase
import com.tinkoff.store.domain.usecase.auth.CustomerSignUpUseCase
import com.tinkoff.store.domain.usecase.auth.SellerSignUpUseCase
import com.tinkoff.store.domain.usecase.auth.SignInUseCase
import com.tinkoff.store.domain.usecase.cart.AddToCartUseCase
import com.tinkoff.store.domain.usecase.cart.DeleteFromCartUseCase
import com.tinkoff.store.domain.usecase.cart.GetCartProductsUseCase
import com.tinkoff.store.domain.usecase.category.GetCategoriesUseCase
import com.tinkoff.store.domain.usecase.order.CreateOrderUseCase
import com.tinkoff.store.domain.usecase.products.*
import com.tinkoff.store.domain.usecase.token.DeleteTokenUseCase
import kotlinx.coroutines.Dispatchers
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
