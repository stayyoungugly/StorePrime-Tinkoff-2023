package com.tinkoff.store.domain.repository

import com.tinkoff.store.domain.entity.Product

interface ProductsRepository {
    suspend fun getMainProducts(): Result<List<Product>>
    suspend fun getMainProduct(): Result<Product>
    suspend fun searchByQuery(query: String): Result<List<Product>>
    suspend fun searchByQueryAndCategory(query: String, category: String): Result<List<Product>>
    suspend fun searchByCategory(category: String): Result<List<Product>>
    suspend fun getProductById(id: Int): Result<Product>
}
