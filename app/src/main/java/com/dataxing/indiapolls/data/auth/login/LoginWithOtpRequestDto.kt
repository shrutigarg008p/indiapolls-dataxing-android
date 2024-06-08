package com.dataxing.indiapolls.data.auth.login

data class LoginWithOtpRequestDto(
    val email: String,
    val phoneNumber: String,
    val registerType: String
)