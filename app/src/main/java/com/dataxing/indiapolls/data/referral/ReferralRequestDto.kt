package com.dataxing.indiapolls.data.referral

class ReferralRequestDto(
    val userId: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val referralStatus: String = "Invited",
    val referralMethod: String = "Manual",
)