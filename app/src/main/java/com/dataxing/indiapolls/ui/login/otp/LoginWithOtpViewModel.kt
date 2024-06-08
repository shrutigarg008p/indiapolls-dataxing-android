package com.dataxing.indiapolls.ui.login.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.data.auth.login.LoginWithOtpRequestDto
import com.dataxing.indiapolls.data.auth.login.UserInfo
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncResult
import com.dataxing.indiapolls.network.map
import com.dataxing.indiapolls.ui.util.isMobileNumberValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginWithOtpViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _formState = MutableStateFlow(LoginWithOtpFormState())
    val formState: StateFlow<LoginWithOtpFormState> = _formState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _result = MutableStateFlow<Result<LoggedInWithOtpUserView>>(Result.initialized())
    val result: StateFlow<Result<LoggedInWithOtpUserView>> = _result.asStateFlow()

    fun login(mobileNumber: String)  {
        dataChanged(mobileNumber)
        if (!formState.value.isDataValid) {
            return
        }

        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val result = asyncResult {
                    RetrofitHelper.apiService.loginWithMobile(LoginWithOtpRequestDto("$mobileNumber@gmail.com",mobileNumber,"mobile"))
                }
                result.dataOrNull()?.let {
                    userPreferencesRepository.updateUserInformation(
                        UserInfo(
                            it.userId,
                            it.email,
                            it.phoneNumber,
                            "mobile",
                            "",
                            true,
                            true,
                            it.token,
                            null,
                        )
                    )
                }

                val view = result.map {
                    LoggedInWithOtpUserView(it.email, it.phoneNumber,false, true)
                }
                _result.update { view }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun reInitializeResult() {
        _result.update { Result.initialized() }
    }

   private fun dataChanged(mobileNumber: String) {
        if (!mobileNumber.isMobileNumberValid) {
            _formState.value = LoginWithOtpFormState(mobileNumberError = R.string.invalid_mobile_number)
        } else {
            _formState.value = LoginWithOtpFormState(isDataValid = true)
        }
    }
}