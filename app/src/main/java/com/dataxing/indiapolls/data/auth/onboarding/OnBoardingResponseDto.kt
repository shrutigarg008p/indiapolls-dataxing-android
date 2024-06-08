package com.dataxing.indiapolls.data.auth.onboarding

import com.dataxing.indiapolls.data.auth.login.BasicProfile
import com.google.gson.annotations.SerializedName

data class OnBoardingResponseDto(
    val id: String,
    val deleteRequestDate: String?,
    val deleteConfirmDate: String?,
    val phoneNumber: String,
    val email: String,
    val emailConfirmed: Boolean,
    val passwordHash: String,
    val registerType: String,
    val securityStamp: String,
    val role: String,
    val phoneNumberConfirmed: Boolean,
    val twoFactorEnabled: Boolean,
    val lockoutEndDateUtc: String?,
    val lockoutEnabled: Boolean,
    val accessFailedCount: Int,
    val username: Any?,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: Any?,
    val activeStatus: Int,
    val signupIp: String,
    val lastUpdatedProfileId: String?,
    val unsubscribeDate: String?,
    val unsubscribeRequestDate: String?,
    val legacyUserId: String?,
    val legacyPassword: String?,
    val legacyError: String?,
    val registeredDate: String,
    @SerializedName("basic_profile")
    val basicProfile: BasicProfile,
)