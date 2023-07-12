package com.tinkoff.store.presentation.person.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinkoff.store.domain.entity.Account
import com.tinkoff.store.domain.usecase.account.GetAccountUseCase
import com.tinkoff.store.domain.usecase.token.DeleteTokenUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class AccountViewModel(
    private val getAccountUseCase: GetAccountUseCase,
    private val deleteTokenUseCase: DeleteTokenUseCase
) : ViewModel() {

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

    fun onQuitClick() {
        viewModelScope.launch {
            try {
                deleteTokenUseCase()
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }
}
