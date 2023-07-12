package com.tinkoff.store.presentation.seller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinkoff.store.domain.entity.CartProduct
import com.tinkoff.store.domain.usecase.cart.DeleteFromCartUseCase
import com.tinkoff.store.domain.usecase.cart.GetCartProductsUseCase
import com.tinkoff.store.domain.usecase.order.CreateOrderUseCase
import com.tinkoff.store.presentation.viewmodel.SingleLiveEvent
import kotlinx.coroutines.launch

class SellerProductsViewModel(
    private val deleteFromCartUseCase: DeleteFromCartUseCase,
    private val getCartProductsUseCase: GetCartProductsUseCase,
    private val createOrderUseCase: CreateOrderUseCase
) : ViewModel() {

    private var _cartProductsList: SingleLiveEvent<Result<List<CartProduct>>?> = SingleLiveEvent()
    val cartProductsList: LiveData<Result<List<CartProduct>>?> = _cartProductsList
    fun onGetCartProducts() {
        viewModelScope.launch {
            try {
                _cartProductsList.value = getCartProductsUseCase()
            } catch (ex: Exception) {
                _cartProductsList.value = Result.failure(ex)
            }
        }
    }

}
