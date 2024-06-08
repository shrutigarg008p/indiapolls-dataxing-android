package com.dataxing.indiapolls.ui.refer

data class ReferralFormState(
    val nameError: Int? = null,
    val emailError: Int? = null,
    val mobileError: Int? = null,
    val isDataValid: Boolean = false
)