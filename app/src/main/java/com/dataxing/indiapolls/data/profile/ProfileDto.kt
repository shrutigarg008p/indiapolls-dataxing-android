package com.dataxing.indiapolls.data.profile

import com.dataxing.indiapolls.data.auth.login.BasicProfile

data class ProfileDto(
    val isNewRecord: Boolean,
    val profile: BasicProfile?,
    var dashboardMessage: DashboardMessage?,
)