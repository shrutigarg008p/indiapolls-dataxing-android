package com.dataxing.indiapolls.ui.login

data class LoginFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)