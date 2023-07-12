package com.tinkoff.store.data.response

import com.tinkoff.store.data.request.LocationForm

data class ProductResponse(
    val id: Int,
    val amount: Int,
    val categories: List<String>,
    val description: String,
    val imageIds: List<String>,
    val price: Double,
    val sellerId: Int,
    val sellerLocation: LocationForm,
    val sellerName: String,
    val title: String
)
