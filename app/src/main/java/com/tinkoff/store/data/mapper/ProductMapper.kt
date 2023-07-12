package com.tinkoff.store.data.mapper

import com.google.gson.Gson
import com.tinkoff.store.BuildConfig
import com.tinkoff.store.MyApplication
import com.tinkoff.store.R
import com.tinkoff.store.data.response.ErrorResponse
import com.tinkoff.store.data.response.ProductResponse
import com.tinkoff.store.domain.entity.Product
import com.tinkoff.store.domain.exception.ServerException
import retrofit2.Response

private const val BASE_URL = BuildConfig.SERVER_URL + "/photos/"

class ProductMapper {
    fun mapToResult(response: Response<ProductResponse>): Result<Product> {
        return if (response.isSuccessful) {
            val body = requireNotNull(response.body())
            Result.success(
                mapToProduct(body)
            )
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

    fun mapToResultList(response: Response<List<ProductResponse>>): Result<List<Product>> {
        return if (response.isSuccessful) {
            val body = requireNotNull(response.body())
            val list = ArrayList<Product>()
            for (product in body) {
                list.add(mapToProduct(product))
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

    private fun mapToProduct(response: ProductResponse): Product {
        val ids = response.imageIds
        val urls = ArrayList<String>()
        for (id in ids) {
            urls.add(generateUrl(id))
        }
        var category: String? = null
        if (response.categories.isNotEmpty()) {
            when (response.categories[0]) {
                "Игрушки" -> category = MyApplication.appContext.getString(R.string.category_toys)
                "Мебель" -> category =
                    MyApplication.appContext.getString(R.string.category_furniture)
                "Продукты" -> category =
                    MyApplication.appContext.getString(R.string.category_products)
                "Зоотовары" -> category = MyApplication.appContext.getString(R.string.category_zoo)
                "Спорт" -> category = MyApplication.appContext.getString(R.string.category_sport)
                "Бытовая техника" -> category =
                    MyApplication.appContext.getString(R.string.category_home)
                "Школа" -> category = MyApplication.appContext.getString(R.string.category_school)
                "Ювелирные изделия" -> category =
                    MyApplication.appContext.getString(R.string.category_jew)
                "Сад и дача" -> category =
                    MyApplication.appContext.getString(R.string.category_garden)
                "Здоровье" -> category =
                    MyApplication.appContext.getString(R.string.category_health)
                "Путешествия" -> category =
                    MyApplication.appContext.getString(R.string.category_travel)
                "Книги" -> category = MyApplication.appContext.getString(R.string.category_books)
                "Электроника" -> category =
                    MyApplication.appContext.getString(R.string.category_tech)
                "Автотовары" -> category = MyApplication.appContext.getString(R.string.category_car)
            }
        }
        return Product(
            id = response.id,
            sellerId = response.sellerId,
            imageUrls = urls,
            category = category,
            title = response.title,
            description = response.description,
            amount = response.amount,
            price = response.price.toInt(),
            sellerCity = response.sellerLocation.city,
            sellerCountry = response.sellerLocation.country,
            sellerName = response.sellerName
        )
    }

    private fun generateUrl(id: String): String {
        return BASE_URL + id
    }
}


