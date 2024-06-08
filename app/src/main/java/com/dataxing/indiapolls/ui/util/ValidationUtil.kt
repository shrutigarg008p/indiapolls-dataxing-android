package com.dataxing.indiapolls.ui.util

import android.util.Patterns

val String.isEmailValid: Boolean
    get () {
        return if (this.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(this).matches()
        } else {
            this.isValid && this.contains("@")
        }
    }

val String.isValid: Boolean
    get () {
        return this.isNotBlank()
    }

val String.isMobileNumberValid: Boolean
    get () {
        return this.isValid && this.length == 10
    }

fun String.isConfirmPasswordValid(confirmPassword: String): Boolean {
    return this.isValid && this == confirmPassword
}