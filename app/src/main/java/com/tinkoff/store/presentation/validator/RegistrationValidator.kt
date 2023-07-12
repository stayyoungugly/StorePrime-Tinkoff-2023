package com.tinkoff.store.presentation.validator

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern.compile

class RegistrationValidator {
    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        val passwordPattern =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\^?!=.*\\[\\]\\/@\$%#&_-])[a-zA-Z\\d\\^?!=.*\\[\\]\\/@\$%#&_-]{8,64}\$"
        return !TextUtils.isEmpty(password) && compile(passwordPattern).matcher(password).matches()
    }

    fun isValidName(name: String): Boolean {
        val namePattern = "^[^\\d\\W]{1,100}\$|^[\\p{L}\\s]{1,100}\$"
        return !TextUtils.isEmpty(name) && compile(namePattern).matcher(name).matches()
    }

    fun isValidDate(date: String): Boolean {
        val namePattern = "^\\d{4}-\\d{2}-\\d{2}\$"
        return !TextUtils.isEmpty(date) && compile(namePattern).matcher(date).matches()
    }

    fun isValidPhoneNumber(number: String): Boolean {
        val namePattern = "^(?:\\+?7|8)?\\d{10}\$"
        return !TextUtils.isEmpty(number) && compile(namePattern).matcher(number).matches()
    }

    fun isValidAddress(address: String): Boolean {
        val namePattern =
            "^(?!(?:\\d+|\\p{L}+\\d+))(?:\\p{L}+(?:,\\s\\p{L}+){2}),\\s\\d+(?:,\\s(\\p{L}+|\\d+))?\$"
        return !TextUtils.isEmpty(address) && compile(namePattern).matcher(address).matches()
    }

    fun isValidLocation(location: String): Boolean {
        val namePattern = "^\\p{L}+(?:,\\s\\p{L}+)?\$"
        return !TextUtils.isEmpty(location) && compile(namePattern).matcher(location).matches()
    }

    fun isValidDescription(desc: String): Boolean {
        val namePattern = "^.{1,100}\$"
        return !TextUtils.isEmpty(desc) && compile(namePattern).matcher(desc).matches()
    }

    fun isValidINN(inn: String): Boolean {
        val namePattern = "^\\d{10}\$"
        return !TextUtils.isEmpty(inn) && compile(namePattern).matcher(inn).matches()
    }
}
