package com.dataxing.indiapolls.data.reward

class RedeemPointsRequestDto(
    val userId: String,
    val redemptionModeTitle: String,
    val redemptionModeId: String,
    val pointsRequested: Long,
    val pointsRedeemed: Long,
    val redemptionRequestStatus: String,
    val redemptionDate: String,
    val notes: String?,
)