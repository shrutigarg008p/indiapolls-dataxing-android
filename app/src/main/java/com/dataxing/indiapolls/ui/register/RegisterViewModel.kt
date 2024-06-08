package com.dataxing.indiapolls.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.auth.register.RegisterRequestDto
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncApiResult
import com.dataxing.indiapolls.network.map
import com.dataxing.indiapolls.ui.util.isConfirmPasswordValid
import com.dataxing.indiapolls.ui.util.isEmailValid
import com.dataxing.indiapolls.ui.util.isMobileNumberValid
import com.dataxing.indiapolls.ui.util.isValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _registerForm = MutableStateFlow(RegisterFormState())
    val registerFormState: StateFlow<RegisterFormState> = _registerForm.asStateFlow()

    private val _result = MutableStateFlow<Result<RegisteredUserView>>(Result.initialized())
    val result: StateFlow<Result<RegisteredUserView>> = _result.asStateFlow()

    fun register(email: String, mobileNumber: String, password: String, confirmPassword: String, isTermsAndConditionChecked: Boolean) {
        dataChanged(email, mobileNumber, password, confirmPassword, isTermsAndConditionChecked)
        if (!registerFormState.value.isDataValid) {
            return
        }

        _isLoading.update { true }
        viewModelScope.launch {
            try {
              val result = asyncApiResult {
                    val requestDto =
                        RegisterRequestDto(email, mobileNumber, password, "panelist", "password")
                    RetrofitHelper.apiService.register(requestDto)
                }

                val view = result.map { RegisteredUserView(it.message, it.data.userId) }
                _result.update { view }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun reInitializeResult() {
        _result.update { Result.initialized() }
    }

    private fun dataChanged(email: String, mobileNumber: String, password: String, confirmPassword: String, isTermsAndConditionChecked: Boolean) {
        if (!email.isEmailValid) {
            _registerForm.value = RegisterFormState(emailError = R.string.invalid_email)
        } else if (!mobileNumber.isMobileNumberValid) {
            _registerForm.value = RegisterFormState(mobileNumberError = R.string.invalid_mobile_number)
        } else if (!password.isValid) {
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_password)
        } else if (!password.isConfirmPasswordValid(confirmPassword)) {
            _registerForm.value = RegisterFormState(confirmPasswordError = R.string.invalid_confirm_password)
        } else if (!isTermsAndConditionChecked) {
            _registerForm.value = RegisterFormState(termAndConditionError = R.string.invalid_terms_and_conditions)
        } else {
            _registerForm.value = RegisterFormState(isDataValid = true)
        }
    }
}