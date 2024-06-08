package com.dataxing.indiapolls.data.auth.onboarding

data class OnBoardingRequestDto(
    var firstName: String? = null,
    var lastName: String? = null,
    var mobile: String? = null,
    var gender: String? = null,
    var dateOfBirth: String? = null,
    var addressLine1: String? = null,
    var addressLine2: String? = null,
    var country: String? = null,
    var state: String? = null,
    var city: String? = null,
    var referralSource: String? = null,
    var pinCode: String? = null,
    var acceptTerms: Boolean? = null,
    var imagePath: String? = null,
)