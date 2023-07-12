package com.tinkoff.store.presentation.product.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinkoff.store.domain.entity.CartProduct
import com.tinkoff.store.domain.entity.Product
import com.tinkoff.store.domain.usecase.cart.AddToCartUseCase
import com.tinkoff.store.domain.usecase.cart.DeleteFromCartUseCase
import com.tinkoff.store.domain.usecase.products.GetProductByIdUseCase
import com.tinkoff.store.presentation.viewmodel.SingleLiveEvent
import kotlinx.coroutines.launch

class ProductFragmentViewModel(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val deleteFromCartUseCase: DeleteFromCartUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private var _error: SingleLiveEvent<Exception> = SingleLiveEvent()
    val error: LiveData<Exception> = _error

    private var _mainProduct: MutableLiveData<Result<Product>?> = MutableLiveData()
    val mainProduct: LiveData<Result<Product>?> = _mainProduct

    private var _cartProduct: SingleLiveEvent<Result<CartProduct>?> = SingleLiveEvent()
    val cartProduct: LiveData<Result<CartProduct>?> = _cartProduct

    fun onGetProductById(id: Int) {
        viewModelScope.launch {
            try {
                _mainProduct.value = getProductByIdUseCase(id)
            } catch (ex: Exception) {
                _mainProduct.value = Result.failure(ex)
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

    fun onDeleteFromCartClick(id: Int) {
        viewModelScope.launch {
            try {
                deleteFromCartUseCase(id)
            } catch (ex: Exception) {
                _error.value = ex
            }
        }
    }
}
