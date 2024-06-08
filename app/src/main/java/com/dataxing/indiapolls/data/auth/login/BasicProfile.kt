package com.dataxing.indiapolls.data.auth.login

data class BasicProfile(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val mobile: String,
    val dateOfBirth: String,
    val referralSource: String?,
    val addressLine1: String,
    val addressLine2: String,
    val country: String,
    val state: String,
    val city: String,
    val pinCode: String,
    val acceptTerms: Boolean,
    val createdAt: String?,
    val updatedAt: String?,
    val deletedAt: String?,
    val registrationIp: String?,
    val imagePath: String?,
)