package com.dataxing.indiapolls.data.profiles

import com.google.gson.annotations.SerializedName

data class Profiles(
    val id: String,
    val name: String,
    val hindi: String,
    val image: String?,
    val description: String,
    val displayOrder: Long,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: Any?,
    val questionCount: String,
    @SerializedName("profileuserresponses.id")
    val profileuserresponsesId: Any?,
    @SerializedName("profileuserresponses.response")
    val profileuserresponsesResponse: Any?,
    val totalQuestions: Long,
    val attemptedQuestions: Long,
    val remainingQuestions: Long,
    val attemptedPercentage: Long,
)