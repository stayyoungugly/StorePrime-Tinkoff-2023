package com.tinkoff.store.presentation.catalog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinkoff.store.domain.entity.Category
import com.tinkoff.store.domain.usecase.category.GetCategoriesUseCase
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private var _categoryList: MutableLiveData<Result<List<Category>>?> = MutableLiveData()
    val categoryList: LiveData<Result<List<Category>>?> = _categoryList

    fun onGetCategories() {
        viewModelScope.launch {
            try {
                _categoryList.value = getCategoriesUseCase()
            } catch (ex: Exception) {
                _categoryList.value = Result.failure(ex)
            }
        }
    }
}
