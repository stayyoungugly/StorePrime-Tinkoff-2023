package com.tinkoff.store.domain.exception

import com.tinkoff.store.MyApplication
import com.tinkoff.store.R

class ServerException : Exception {
    constructor() : super()
    constructor(message: String) : super(processMessage(message))
    constructor(message: String, cause: Throwable) : super(processMessage(message), cause)
    constructor(cause: Throwable) : super(cause)

    companion object {
        private fun processMessage(message: String): String {
            return when (message) {
                "CART_ITEM_NOT_FOUND" -> MyApplication.appContext.getString(R.string.error_item_not_found)

                "CATEGORY_NOT_FOUND" -> MyApplication.appContext.getString(R.string.error_category_not_found)

                "CUSTOMER_NOT_FOUND" -> MyApplication.appContext.getString(R.string.error_customer_not_found)

                "PHOTO_NOT_FOUND" -> MyApplication.appContext.getString(R.string.error_photo_not_found)

                "PRODUCT_NOT_FOUND" -> MyApplication.appContext.getString(R.string.error_product_not_found)

                "SELLER_NOT_FOUND" -> MyApplication.appContext.getString(R.string.error_seller_not_found)

                "DISPARATE_DATA" -> MyApplication.appContext.getString(R.string.error_disparate_data)

                "PAYMENT_IMPOSSIBLE" -> MyApplication.appContext.getString(R.string.error_payment_impossible)

                "ACCESS_DENIED" -> MyApplication.appContext.getString(R.string.error_access_denie)

                "ALREADY_EXIST" -> MyApplication.appContext.getString(R.string.error_email_exist)

                "VALIDATION_REJECTED" -> MyApplication.appContext.getString(R.string.error_validation)

                "INTERNAL_SERVER_ERROR" -> MyApplication.appContext.getString(R.string.error_server_error)

                else -> message
            }
        }
    }
}
