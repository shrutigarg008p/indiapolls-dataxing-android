package com.dataxing.indiapolls.data.auth.register

data class RegisterResponseDto(
    val email: String,
    val userId: String,
    val phoneNumber: String,
)