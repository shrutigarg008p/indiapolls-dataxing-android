package com.dataxing.indiapolls.data.auth

data class VerifyOtpRequestDto(
    val userId: String,
    val otp: String,
)