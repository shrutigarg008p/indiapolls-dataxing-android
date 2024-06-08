package com.dataxing.indiapolls.data.auth.login

import com.google.gson.annotations.SerializedName

public class LoginWithFacebookRequestDto(
    val email: String,
    @SerializedName("facebooktoken")
    val token: String,
    val role: String,
    val registerType: String
)