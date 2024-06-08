package com.dataxing.indiapolls.data.password

data class ChangePasswordRequestDto(
    val userId: String,
    val currentPassword: String,
    val newPassword: String,
)