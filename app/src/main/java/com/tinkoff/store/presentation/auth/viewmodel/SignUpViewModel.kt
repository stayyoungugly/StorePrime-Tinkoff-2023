package com.tinkoff.store.presentation.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinkoff.store.domain.entity.Customer
import com.tinkoff.store.domain.entity.Seller
import com.tinkoff.store.domain.usecase.auth.CustomerSignUpUseCase
import com.tinkoff.store.domain.usecase.auth.SellerSignUpUseCase
import com.tinkoff.store.presentation.viewmodel.SingleLiveEvent
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val customerSignUpUseCase: CustomerSignUpUseCase,
    private val sellerSignUpUseCase: SellerSignUpUseCase
) : ViewModel() {
    private var _customerSignUpResult: SingleLiveEvent<Result<Customer>?> = SingleLiveEvent()
    val customerSignUpResult: LiveData<Result<Customer>?> = _customerSignUpResult

    private var _sellerSignUpResult: SingleLiveEvent<Result<Seller>?> = SingleLiveEvent()
    val sellerSignUpResult: LiveData<Result<Seller>?> = _sellerSignUpResult

    fun onSignUpClick(
        birthdayDate: String,
        email: String,
        gender: String,
        name: String,
        password: String,
        phoneNumber: String,
        surname: String,
        address: String
    ) {
        viewModelScope.launch {
            try {
                val listAddress = parseString(address)
                var apartmentNumber: String? = null
                if (listAddress.size > 4) {
                    apartmentNumber = listAddress[4]
                }
                println(listAddress)
                _customerSignUpResult.value =
                    customerSignUpUseCase(
                        birthdayDate,
                        email,
                        gender,
                        name,
                        password,
                        phoneNumber,
                        surname,
                        country = listAddress[0],
                        city = listAddress[1],
                        street = listAddress[2],
                        house = listAddress[3].toInt(),
                        apartment = apartmentNumber
                    )
            } catch (ex: Exception) {
                _customerSignUpResult.value = Result.failure(ex)
            }
        }
    }

    fun onSignUpClick(
        description: String,
        email: String,
        password: String,
        inn: String,
        address: String,
        name: String,
        phoneNumber: String
    ) {
        viewModelScope.launch {
            try {
                val listLocation = parseString(address)
                println(listLocation)
                _sellerSignUpResult.value =
                    sellerSignUpUseCase(
                        description,
                        email,
                        password,
                        inn,
                        listLocation[1],
                        listLocation[0],
                        name,
                        phoneNumber
                    )
            } catch (ex: Exception) {
                _sellerSignUpResult.value = Result.failure(ex)
            }
        }
    }

    private fun parseString(inputString: String): List<String> {
        val regexPattern = Regex("""\s*,\s*""")
        return regexPattern.split(inputString)
    }
}
