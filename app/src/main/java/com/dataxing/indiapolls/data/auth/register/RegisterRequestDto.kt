package com.dataxing.indiapolls.data.auth.register

data class RegisterRequestDto(
    val email: String,
    val phoneNumber: String,
    val password: String,
    val role: String,
    val registerType: String
    )