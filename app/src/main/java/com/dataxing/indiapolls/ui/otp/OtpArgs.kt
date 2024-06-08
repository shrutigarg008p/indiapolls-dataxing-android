package com.dataxing.indiapolls.ui.otp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OtpArgs(val mobileNumber: String, val completionNavigation: CompletionNavigation, val userId: String? = null): Parcelable