package com.dataxing.indiapolls.ui.onboarding.register

data class OnBoardingBasicInformationFormState(
    val firstNameError: Int? = null,
    val lastNameError: Int? = null,
    val mobileNumberError: Int? = null,
    val genderError: Int? = null,
    val dateOfBirthError: Int? = null,
    val isDataValid: Boolean = false
)