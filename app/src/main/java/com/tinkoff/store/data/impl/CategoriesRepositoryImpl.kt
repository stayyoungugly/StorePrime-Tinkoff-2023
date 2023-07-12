package com.tinkoff.store.data.impl

import com.tinkoff.store.data.api.NoAuthApi
import com.tinkoff.store.data.mapper.CategoryMapper
import com.tinkoff.store.domain.entity.Category
import com.tinkoff.store.domain.repository.CategoriesRepository

class CategoriesRepositoryImpl(
    val api: NoAuthApi,
    val mapper: CategoryMapper
): CategoriesRepository{
    override suspend fun getCategories(): Result<List<Category>> {
        return mapper.mapToResultList(api.getCategories())
    }
}
