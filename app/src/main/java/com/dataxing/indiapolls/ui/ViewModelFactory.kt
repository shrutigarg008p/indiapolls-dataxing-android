package com.dataxing.indiapolls.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.dataxing.indiapolls.ui.dashboard.DashboardViewModel
import com.dataxing.indiapolls.ui.home.HomeViewModel
import com.dataxing.indiapolls.ui.login.LoginViewModel
import com.dataxing.indiapolls.ui.login.otp.LoginWithOtpViewModel
import com.dataxing.indiapolls.ui.onboarding.OnBoardingViewModel
import com.dataxing.indiapolls.ui.otp.VerifyOtpViewModel
import com.dataxing.indiapolls.ui.password.change.ChangePasswordViewModel
import com.dataxing.indiapolls.ui.password.forgot.ForgotPasswordViewModel
import com.dataxing.indiapolls.ui.profile.ProfileViewModel
import com.dataxing.indiapolls.ui.profiles.ProfilesViewModel
import com.dataxing.indiapolls.ui.profiles.survey.ProfilesSurveyViewModel
import com.dataxing.indiapolls.ui.refer.ReferralViewModel
import com.dataxing.indiapolls.ui.register.RegisterViewModel
import com.dataxing.indiapolls.ui.reward.RewardViewModel
import com.dataxing.indiapolls.ui.reward.redeem.RedeemPointsViewModel
import com.dataxing.indiapolls.ui.support.ContactUsViewModel
import com.dataxing.indiapolls.ui.survey.SurveyViewModel
import com.dataxing.indiapolls.ui.util.userPreferencesRepository

class ViewModelFactory  : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as MainApplication
        val userPreferencesRepository = application.userPreferencesRepository()
        return when(modelClass) {
            LoginViewModel::class.java -> { LoginViewModel(userPreferencesRepository) }
            LoginWithOtpViewModel::class.java -> { LoginWithOtpViewModel(userPreferencesRepository) }
            ForgotPasswordViewModel::class.java -> { ForgotPasswordViewModel() }
            VerifyOtpViewModel::class.java -> { VerifyOtpViewModel(userPreferencesRepository) }
            OnBoardingViewModel::class.java -> { OnBoardingViewModel(userPreferencesRepository) }
            RegisterViewModel::class.java -> { RegisterViewModel() }
            HomeViewModel::class.java -> { HomeViewModel(userPreferencesRepository) }
            SurveyViewModel::class.java -> { SurveyViewModel(userPreferencesRepository) }
            DashboardViewModel::class.java -> { DashboardViewModel(userPreferencesRepository) }
            ProfilesViewModel::class.java -> { ProfilesViewModel(userPreferencesRepository) }
            ProfilesSurveyViewModel::class.java -> { ProfilesSurveyViewModel(userPreferencesRepository) }
            RewardViewModel::class.java -> { RewardViewModel(userPreferencesRepository) }
            RedeemPointsViewModel::class.java -> { RedeemPointsViewModel(userPreferencesRepository) }
            ContactUsViewModel::class.java -> { ContactUsViewModel(userPreferencesRepository) }
            ReferralViewModel::class.java -> { ReferralViewModel(userPreferencesRepository) }
            ProfileViewModel::class.java -> { ProfileViewModel(userPreferencesRepository) }
            ChangePasswordViewModel::class.java -> { ChangePasswordViewModel(userPreferencesRepository) }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    }
}