package com.dataxing.indiapolls.ui.login.otp

data class LoggedInWithOtpUserView(
    val message: String,
    val phoneNumber: String,
    val isOnBoardingCompleted: Boolean,
    val isPhoneNumberConfirmed: Boolean,
)