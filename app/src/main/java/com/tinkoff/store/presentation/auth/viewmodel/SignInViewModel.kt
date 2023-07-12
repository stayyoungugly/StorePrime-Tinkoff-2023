package com.tinkoff.store.presentation.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinkoff.store.domain.entity.Account
import com.tinkoff.store.domain.entity.Token
import com.tinkoff.store.domain.usecase.account.GetAccountUseCase
import com.tinkoff.store.domain.usecase.auth.SignInUseCase
import com.tinkoff.store.presentation.viewmodel.SingleLiveEvent
import kotlinx.coroutines.launch

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {

    private var _signInResult: SingleLiveEvent<Result<Token>?> = SingleLiveEvent()
    val signInResult: LiveData<Result<Token>?> = _signInResult

    private var _account: MutableLiveData<Result<Account>?> = MutableLiveData()
    val account: LiveData<Result<Account>?> = _account

    fun onGetAccount() {
        viewModelScope.launch {
            try {
                _account.value = getAccountUseCase()
            } catch (ex: Exception) {
                _account.value = Result.failure(ex)
            }
        }
    }

    fun onLoginClick(email: String, password: String) {
        viewModelScope.launch {
            try {
                _signInResult.value = signInUseCase(email, password)
            } catch (ex: Exception) {
                _signInResult.value = Result.failure(ex)
            }
        }
    }
}
