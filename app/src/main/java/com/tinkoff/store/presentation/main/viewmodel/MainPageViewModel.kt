package com.tinkoff.store.presentation.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinkoff.store.domain.entity.CartProduct
import com.tinkoff.store.domain.entity.Product
import com.tinkoff.store.domain.usecase.cart.AddToCartUseCase
import com.tinkoff.store.domain.usecase.products.MainProductUseCase
import com.tinkoff.store.domain.usecase.products.MainProductsUseCase
import com.tinkoff.store.presentation.viewmodel.SingleLiveEvent
import kotlinx.coroutines.launch

class MainPageViewModel(
    private val mainProductsUseCase: MainProductsUseCase,
    private val mainProductUseCase: MainProductUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private var _mainProductsList: MutableLiveData<Result<List<Product>>?> = MutableLiveData()
    val mainProductsList: LiveData<Result<List<Product>>?> = _mainProductsList

    private var _mainProduct: MutableLiveData<Result<Product>?> = MutableLiveData()
    val mainProduct: LiveData<Result<Product>?> = _mainProduct

    private var _cartProduct: SingleLiveEvent<Result<CartProduct>?> = SingleLiveEvent()
    val cartProduct: LiveData<Result<CartProduct>?> = _cartProduct

    fun onGetMainProduct() {
        viewModelScope.launch {
            try {
                _mainProduct.value = mainProductUseCase()
            } catch (ex: Exception) {
                _mainProduct.value = Result.failure(ex)
            }
        }

    }

    fun onGetMainProducts() {
        viewModelScope.launch {
            try {
                _mainProductsList.value = mainProductsUseCase()
            } catch (ex: Exception) {
                _mainProductsList.value = Result.failure(ex)
            }
        }
    }

    fun onAddToCartClick(id: Int) {
        viewModelScope.launch {
            try {
                _cartProduct.value = addToCartUseCase(id, 1)
            } catch (ex: Exception) {
                _cartProduct.value = Result.failure(ex)
            }
        }
    }
}
