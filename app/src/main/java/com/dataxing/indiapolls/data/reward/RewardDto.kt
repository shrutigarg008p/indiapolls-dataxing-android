package com.dataxing.indiapolls.data.reward

import com.dataxing.indiapolls.data.Survey
import com.dataxing.indiapolls.data.User
import com.google.gson.annotations.SerializedName

data class RewardDto(
    @SerializedName("data")
    val items: List<RewardItemDto>,
    val totalPointsInfo: List<TotalPointInfo>,
)

data class RewardItemDto(
    val id: String,
    val userId: String,
    val rewardDate: String,
    val points: Long,
    val rewardType: String,
    val surveyId: String,
    val referralId: String?,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
    val rewardStatus: String,
    val revokeDate: String?,
    val revokedById: String?,
    val grantedById: String?,
    val sweepstakeId: String?,
    val survey: Survey?,
    val user: User,
)

data class TotalPointInfo(
    val name: String,
    val value: Long,
)

