package com.dataxing.indiapolls.ui.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.data.auth.ResendOtpRequestDto
import com.dataxing.indiapolls.data.auth.VerifyOtpRequestDto
import com.dataxing.indiapolls.data.auth.login.UserInfo
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncApiResultWithUserId
import com.dataxing.indiapolls.network.asyncResultWithUserInfo
import com.dataxing.indiapolls.network.map
import com.dataxing.indiapolls.network.toMessageResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VerifyOtpViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _result = MutableStateFlow<Result<String>>(Result.initialized())
    val result: StateFlow<Result<String>> = _result.asStateFlow()

    private val _isOnBoardingResult = MutableStateFlow<Result<Boolean>>(Result.initialized())
    val isOnBoardingResult: StateFlow<Result<Boolean>> = _isOnBoardingResult.asStateFlow()

    private val _resendOtpResult = MutableStateFlow<Result<String>>(Result.initialized())
    val resendOtpResult: StateFlow<Result<String>> = _resendOtpResult.asStateFlow()

    fun verifyOtp(userId: String?, otp: String) {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val view = asyncApiResultWithUserId(userId, userPreferencesRepository) {
                    val requestDto = VerifyOtpRequestDto(it, otp)
                    RetrofitHelper.apiService.verifyOtp(requestDto)
                }.toMessageResult()

                _result.update { view }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun resendOtp(userId: String?, phoneNumber: String) {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val view = asyncApiResultWithUserId(userId, userPreferencesRepository) {
                    val requestDto = ResendOtpRequestDto(it, phoneNumber)
                    RetrofitHelper.apiService.resendOtp(requestDto)
                }.toMessageResult()

                _resendOtpResult.update { view }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun reInitializeResult() {
        _result.update { Result.initialized() }
        _resendOtpResult.update { Result.initialized() }
        _isOnBoardingResult.update { Result.initialized() }
    }

    fun fetchProfile() {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                var info: UserInfo? = null
                val result = asyncResultWithUserInfo(userPreferencesRepository) { it ->
                    info = it
                    RetrofitHelper.apiService.getUser(it.id)
                }
                val data = result.requireData()

                info?.let {
                    userPreferencesRepository.updateUserInformation(it.copy(basicProfile = data.profile))
                }

                val view = result.map {
                    data.profile != null
                }
                _isOnBoardingResult.update { view }
            } finally {
                _isLoading.update { false }
            }
        }
    }
}