package com.tinkoff.store.data.request

data class AddressForm(
    val apartment: String?,
    val house: Int,
    val location: LocationForm,
    val street: String
)
