package com.tinkoff.store.domain.usecase.category

import com.tinkoff.store.domain.entity.Category
import com.tinkoff.store.domain.repository.CategoriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetCategoriesUseCase(
    private val repository: CategoriesRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
    ): Result<List<Category>> {
        return withContext(dispatcher) {
            repository.getCategories()
        }
    }
}
