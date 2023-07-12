package com.tinkoff.store.di

import com.tinkoff.store.presentation.auth.viewmodel.SignInViewModel
import com.tinkoff.store.presentation.auth.viewmodel.SignUpViewModel
import com.tinkoff.store.presentation.cart.viewmodel.CartFragmentViewModel
import com.tinkoff.store.presentation.catalog.viewmodel.CatalogViewModel
import com.tinkoff.store.presentation.main.viewmodel.MainPageViewModel
import com.tinkoff.store.presentation.person.viewmodel.AccountViewModel
import com.tinkoff.store.presentation.product.viewmodel.ProductFragmentViewModel
import com.tinkoff.store.presentation.search.viewmodel.SearchFragmentViewModel
import com.tinkoff.store.presentation.seller.viewmodel.SellerProductsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::MainPageViewModel)
    viewModelOf(::SearchFragmentViewModel)
    viewModelOf(::CatalogViewModel)
    viewModelOf(::ProductFragmentViewModel)
    viewModelOf(::SellerProductsViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::CartFragmentViewModel)

}
