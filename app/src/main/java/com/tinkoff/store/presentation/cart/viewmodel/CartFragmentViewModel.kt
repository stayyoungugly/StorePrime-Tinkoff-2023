package com.tinkoff.store.presentation.cart.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinkoff.store.domain.entity.CartProduct
import com.tinkoff.store.domain.entity.OrderProduct
import com.tinkoff.store.domain.usecase.cart.DeleteFromCartUseCase
import com.tinkoff.store.domain.usecase.cart.GetCartProductsUseCase
import com.tinkoff.store.domain.usecase.order.CreateOrderUseCase
import com.tinkoff.store.presentation.viewmodel.SingleLiveEvent
import kotlinx.coroutines.launch

class CartFragmentViewModel(
    private val deleteFromCartUseCase: DeleteFromCartUseCase,
    private val getCartProductsUseCase: GetCartProductsUseCase,
    private val createOrderUseCase: CreateOrderUseCase
) : ViewModel() {

    private var _error: SingleLiveEvent<Exception> = SingleLiveEvent()
    val error: LiveData<Exception> = _error

    private var _cartProductsList: SingleLiveEvent<Result<List<CartProduct>>?> = SingleLiveEvent()
    val cartProductsList: LiveData<Result<List<CartProduct>>?> = _cartProductsList

    private var _orderList: SingleLiveEvent<Result<List<OrderProduct>>?> = SingleLiveEvent()
    val orderList: LiveData<Result<List<OrderProduct>>?> = _orderList

    fun onDeleteFromCartClick(id: Int) {
        viewModelScope.launch {
            try {
                deleteFromCartUseCase(id)
            } catch (ex: Exception) {
                _error.value = ex
            }
        }
    }

    fun onGetCartProducts() {
        viewModelScope.launch {
            try {
                _cartProductsList.value = getCartProductsUseCase()
            } catch (ex: Exception) {
                _cartProductsList.value = Result.failure(ex)
            }
        }
    }

    fun onBuyProductsClick(ids: ArrayList<Int>) {
        viewModelScope.launch {
            try {
                _orderList.value = createOrderUseCase(ids)
            } catch (ex: Exception) {
                _orderList.value = Result.failure(ex)
            }
        }
    }
}
