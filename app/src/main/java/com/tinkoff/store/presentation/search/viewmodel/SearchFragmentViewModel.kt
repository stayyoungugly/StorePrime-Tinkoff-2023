package com.tinkoff.store.presentation.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinkoff.store.domain.entity.CartProduct
import com.tinkoff.store.domain.entity.Product
import com.tinkoff.store.domain.usecase.cart.AddToCartUseCase
import com.tinkoff.store.domain.usecase.products.*
import com.tinkoff.store.presentation.viewmodel.SingleLiveEvent
import kotlinx.coroutines.launch

class SearchFragmentViewModel(
    private val searchByQueryUseCase: SearchByQueryUseCase,
    private val searchByCategoryUseCase: SearchByCategoryUseCase,
    private val searchByQueryAndCategoryUseCase: SearchByQueryAndCategoryUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private var _productsList: SingleLiveEvent<Result<List<Product>>?> = SingleLiveEvent()
    val productsList: LiveData<Result<List<Product>>?> = _productsList

    private var _cartProduct: SingleLiveEvent<Result<CartProduct>?> = SingleLiveEvent()
    val cartProduct: LiveData<Result<CartProduct>?> = _cartProduct

    fun onSearchByQuery(query: String) {
        viewModelScope.launch {
            try {
                _productsList.value = searchByQueryUseCase(query)
            } catch (ex: Exception) {
                _productsList.value = Result.failure(ex)
            }
        }
    }

    fun onSearchByCategory(category: String) {
        viewModelScope.launch {
            try {
                _productsList.value = searchByCategoryUseCase(category)
            } catch (ex: Exception) {
                _productsList.value = Result.failure(ex)
            }
        }
    }

    fun onSearchByQueryAndCategory(query: String, category: String) {
        viewModelScope.launch {
            try {
                _productsList.value = searchByQueryAndCategoryUseCase(query, category)
            } catch (ex: Exception) {
                _productsList.value = Result.failure(ex)
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
