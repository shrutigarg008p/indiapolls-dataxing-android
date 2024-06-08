package com.dataxing.indiapolls.ui.register

data class RegisterFormState(
    val emailError: Int? = null,
    val mobileNumberError: Int? = null,
    val passwordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val termAndConditionError: Int? = null,
    val isDataValid: Boolean = false
)