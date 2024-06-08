package com.dataxing.indiapolls.data.auth.login

data class UserInfo(
    val id: String,
    val email: String?,
    val phoneNumber: String?,
    val registerType: String,
    val role: String,
    val phoneNumberConfirmed: Boolean,
    val emailConfirmed: Boolean,
    val token: String,
    var basicProfile: BasicProfile?,
)