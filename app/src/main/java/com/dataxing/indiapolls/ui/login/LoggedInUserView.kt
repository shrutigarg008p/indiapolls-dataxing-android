package com.dataxing.indiapolls.ui.login

data class LoggedInUserView(
    val message: String,
    val mobileNumber: String,
    val isOnBoardingCompleted: Boolean,
    val isPhoneNumberConfirmed: Boolean,
)