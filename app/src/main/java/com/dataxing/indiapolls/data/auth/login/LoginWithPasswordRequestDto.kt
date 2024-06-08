package com.dataxing.indiapolls.data.auth.login

data class LoginWithPasswordRequestDto(
    val email: String,
    val password: String,
    val registerType: String
    )