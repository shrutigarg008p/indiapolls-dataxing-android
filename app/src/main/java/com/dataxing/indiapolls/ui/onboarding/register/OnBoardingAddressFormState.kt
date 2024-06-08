package com.dataxing.indiapolls.ui.onboarding.register

data class OnBoardingAddressFormState(
    val addressError: Int? = null,
    val pinCodeError: Int? = null,
    val countryError: Int? = null,
    val stateError: Int? = null,
    val cityError: Int? = null,
    val referralCodeError: Int? = null,
    val isDataValid: Boolean = false
)