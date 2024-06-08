package com.dataxing.indiapolls.data.auth.login

data class LoginWithOtpUserResponseDto(
    val email: String,
    val userId: String,
    val phoneNumber: String,
    val token: String,
)