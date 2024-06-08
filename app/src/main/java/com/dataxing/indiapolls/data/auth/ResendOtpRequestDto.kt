package com.dataxing.indiapolls.data.auth

data class ResendOtpRequestDto(
    val userId: String,
    val phoneNumber: String,
)