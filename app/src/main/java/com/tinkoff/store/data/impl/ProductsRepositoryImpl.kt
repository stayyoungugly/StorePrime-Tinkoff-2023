package com.tinkoff.store.data.impl

import com.tinkoff.store.data.api.NoAuthApi
import com.tinkoff.store.data.mapper.ProductMapper
import com.tinkoff.store.domain.entity.Product
import com.tinkoff.store.domain.repository.ProductsRepository

class ProductsRepositoryImpl(
    val api: NoAuthApi,
    val mapper: ProductMapper,
) : ProductsRepository {
    override suspend fun getMainProducts(): Result<List<Product>> {
        return mapper.mapToResultList(api.getMainProducts(6))
    }

    override suspend fun getMainProduct(): Result<Product> {
        return mapper.mapToResult(api.getMainProduct())
    }

    override suspend fun searchByQuery(query: String): Result<List<Product>> {
        return mapper.mapToResultList(api.searchByQuery(query))
    }

    override suspend fun searchByQueryAndCategory(
        query: String,
        category: String
    ): Result<List<Product>> {
        return mapper.mapToResultList(api.searchByQueryAndCategory(query, category))
    }

    override suspend fun searchByCategory(
        category: String
    ): Result<List<Product>> {
        return mapper.mapToResultList(api.searchByCategory(category))
    }

    override suspend fun getProductById(id: Int): Result<Product> {
        return mapper.mapToResult(api.getProductById(id))
    }

}
