package com.dataxing.indiapolls.data.profiles

import com.dataxing.indiapolls.data.auth.login.BasicProfile

data class ProfilesDto(
    val result: List<Profiles>,
    val overallAttemptedPercentage: Long,
    val basicProfile: BasicProfile?,
    val users: Users?,
)

data class Users(
    val unsubscribeDate: String,
    val id: String,
    val deleteRequestDate: String,
)
