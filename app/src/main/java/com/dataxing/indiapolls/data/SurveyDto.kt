package com.dataxing.indiapolls.data

data class SurveyItemDto(
    val id: String,
    val surveyId: String,
    val userId: String,
    val status: String?,
    val isStarted: Boolean,
    val isCompleted: Boolean,
    val isDisqualified: Boolean,
    val isOverQuota: Boolean,
    val isClosedSurvey: Boolean,
    val isOutlier: Boolean,
    val isRejected: Boolean,
    val pointsRewarded: Long,
    val temporarySurveyLinkId: Long,
    val temporarySurveyLink: String,
    val originalSurveyLink: String,
    val expiryDate: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
    val survey: Survey,
)