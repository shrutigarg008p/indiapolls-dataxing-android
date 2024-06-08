package com.dataxing.indiapolls.ui.password.change

data class ChangePasswordFormState(
    val currentPasswordError: Int? = null,
    val newPasswordError: Int? = null,
    val newConfirmPasswordError: Int? = null,
    val isDataValid: Boolean = false
)