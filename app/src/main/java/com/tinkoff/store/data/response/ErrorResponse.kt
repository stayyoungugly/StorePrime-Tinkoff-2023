package com.tinkoff.store.data.response

data class ErrorResponse(
    val message: String,
    val status: Int,
    val serviceMessage: String
)
