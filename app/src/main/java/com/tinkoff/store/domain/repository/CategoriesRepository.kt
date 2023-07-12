package com.tinkoff.store.domain.repository

import com.tinkoff.store.domain.entity.Category

interface CategoriesRepository {
    suspend fun getCategories(): Result<List<Category>>
}
