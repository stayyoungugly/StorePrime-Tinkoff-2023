package com.tinkoff.store.data.mapper

import com.google.gson.Gson
import com.tinkoff.store.BuildConfig
import com.tinkoff.store.MyApplication
import com.tinkoff.store.R
import com.tinkoff.store.data.response.CategoryResponse
import com.tinkoff.store.data.response.ErrorResponse
import com.tinkoff.store.data.response.ProductResponse
import com.tinkoff.store.domain.entity.Category
import com.tinkoff.store.domain.entity.Product
import com.tinkoff.store.domain.exception.ServerException
import retrofit2.Response

private const val BASE_URL = BuildConfig.SERVER_URL + "/photos/"

class CategoryMapper {
    fun mapToResultList(response: Response<List<CategoryResponse>>): Result<List<Category>> {
        return if (response.isSuccessful) {
            val body = requireNotNull(response.body())
            val list = ArrayList<Category>()
            for (category in body) {
                list.add(mapToCategory(category))
            }
            Result.success(list)
        } else {
            val message = response.message()
            val body = requireNotNull(response.errorBody())
            try {
                val errorResponse: ErrorResponse =
                    Gson().fromJson(body.string(), ErrorResponse::class.java)
                Result.failure(ServerException(errorResponse.serviceMessage))
            } catch (ex: Exception) {
                Result.failure(ServerException(message))
            }
        }
    }

    private fun mapToCategory(response: CategoryResponse): Category {
        var name = ""
        when (response.name) {
            "Игрушки" -> name = MyApplication.appContext.getString(R.string.category_toys)
            "Мебель" -> name = MyApplication.appContext.getString(R.string.category_furniture)
            "Продукты" -> name = MyApplication.appContext.getString(R.string.category_products)
            "Зоотовары" -> name = MyApplication.appContext.getString(R.string.category_zoo)
            "Спорт" -> name = MyApplication.appContext.getString(R.string.category_sport)
            "Бытовая техника" -> name = MyApplication.appContext.getString(R.string.category_home)
            "Школа" -> name = MyApplication.appContext.getString(R.string.category_school)
            "Ювелирные изделия" -> name = MyApplication.appContext.getString(R.string.category_jew)
            "Сад и дача" -> name = MyApplication.appContext.getString(R.string.category_garden)
            "Здоровье" -> name = MyApplication.appContext.getString(R.string.category_health)
            "Путешествия" -> name = MyApplication.appContext.getString(R.string.category_travel)
            "Книги" -> name = MyApplication.appContext.getString(R.string.category_books)
            "Электроника" -> name = MyApplication.appContext.getString(R.string.category_tech)
            "Автотовары" -> name = MyApplication.appContext.getString(R.string.category_car)
        }

        return Category(
            type = response.name,
            imageUrl = generateUrl(response.imageId),
            name = name
        )
    }

    private fun generateUrl(id: String): String {
        return BASE_URL + id
    }
}
