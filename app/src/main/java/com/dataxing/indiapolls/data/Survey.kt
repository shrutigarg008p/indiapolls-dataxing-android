package com.dataxing.indiapolls.data

import com.google.gson.annotations.SerializedName

data class Survey(
    val name: String,
    val description: String,
    val surveyLength: Long?,
    val ceggPoints: Long,
    val expiryDate: String,
    @SerializedName("description_one")
    val descriptionOne: String,
    @SerializedName("description_two")
    val descriptionTwo: String,
    @SerializedName("description_three")
    val descriptionThree: String,
    @SerializedName("description_four")
    val descriptionFour: String,
    val colorcode: String?,
)