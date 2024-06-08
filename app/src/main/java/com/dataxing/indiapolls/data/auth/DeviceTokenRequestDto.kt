package com.dataxing.indiapolls.data.auth

import com.google.gson.annotations.SerializedName

data class DeviceTokenRequestDto(
    val userId: String,
    @SerializedName("devicetoken")
    val token: String,
)