package com.dataxing.indiapolls.data.auth.register

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class RegisteredUser(
    val userId: String,
    val displayName: String
)