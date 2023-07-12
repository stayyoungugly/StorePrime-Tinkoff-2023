package com.tinkoff.store.data.response

import com.tinkoff.store.data.request.LocationForm

data class PictureResponse(
    val id: Int,
    val email: String,
    val phoneNumber: String,
    val cardBalance: String,
    val name: String,
    val description: String,
    val locationDto: LocationForm
)
