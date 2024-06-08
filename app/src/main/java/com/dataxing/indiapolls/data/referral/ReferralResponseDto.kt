package com.dataxing.indiapolls.data.referral

class ReferralResponseDto(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val referralStatus: String,
    val referralMethod: String,
    val userId: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
    val referredUserId: String?,
    val approvalDate: String?,
    val cancellationDate: String?,
    val approvedById: String?,
    val cancelledById: String?,
)