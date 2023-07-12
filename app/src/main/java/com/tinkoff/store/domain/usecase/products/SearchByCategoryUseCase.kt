package com.tinkoff.store.domain.usecase.products

import com.tinkoff.store.domain.entity.Product
import com.tinkoff.store.domain.repository.ProductsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SearchByCategoryUseCase(
    private val repository: ProductsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        category: String
    ): Result<List<Product>> {
        return withContext(dispatcher) {
            repository.searchByCategory(category)
        }
    }
}
