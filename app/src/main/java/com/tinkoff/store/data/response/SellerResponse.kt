package com.tinkoff.store.data.response

import com.tinkoff.store.data.request.LocationForm

data class SellerResponse(
    val id: Int,
    val email: String,
    val phoneNumber: String,
    val cardBalance: Int,
    val name: String,
    val description: String,
    val locationDto: LocationForm
)
