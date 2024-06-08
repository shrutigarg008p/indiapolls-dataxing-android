package com.dataxing.indiapolls.data.profiles

data class SubmitAnswersRequestDto(
    val userId: String,
    val profileId: String,
    val response: Map<String, Any>,
)