package com.dataxing.indiapolls.data.profiles

import com.google.gson.annotations.SerializedName

data class ProfilesSurveyQuestionsDto(
    @SerializedName("Data")
    val data: Info,
    val questions: List<Question>,
    val response: Response,
)

data class Info(
    val id: String,
    val name: String,
    val description: String,
    val displayOrder: Long,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
)

data class Question(
    val id: String,
    val profileId: String,
    val text: String,
    val hint: String,
    val questionId: Long?,
    val displayOrder: Long,
    val isActive: Boolean,
    val displayType: Long,
    val dataType: Long,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
    val options: List<Option>,
)

data class Option(
    val id: String,
    val questionId: String,
    val value: String,
    val hint: String,
    val displayOrder: Long,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
)

data class Response(
    val id: String?,
    val profileId: String?,
    val userId: String?,
    val response: Map<String, Any>?,
    val createdAt: String?,
    val updatedAt: String?,
    val deletedAt: Any?,
)