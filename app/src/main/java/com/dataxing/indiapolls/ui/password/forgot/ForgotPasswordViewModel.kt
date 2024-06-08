package com.dataxing.indiapolls.ui.password.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.password.ForgotPasswordRequestDto
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncApiResult
import com.dataxing.indiapolls.network.toMessageResult
import com.dataxing.indiapolls.ui.util.isEmailValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {
    private val _formState = MutableStateFlow(ForgotPasswordUiState())
    val formState: StateFlow<ForgotPasswordUiState> = _formState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _result = MutableStateFlow<Result<String>>(Result.initialized())
    val result: StateFlow<Result<String>> = _result.asStateFlow()

    fun forgotPassword(email: String) {
        dataChanged(email)
        if (!formState.value.isDataValid) {
            return
        }

        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val view = asyncApiResult {
                    val requestDto = ForgotPasswordRequestDto(email)
                    RetrofitHelper.apiService.forgotPassword(requestDto)
                }.toMessageResult()
                _result.update {view }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun reInitializeResult() {
        _result.update { Result.initialized() }
    }

    private fun dataChanged(email: String) {
        if (!email.isEmailValid) {
            _formState.value = ForgotPasswordUiState(emailError = R.string.invalid_email)
        } else {
            _formState.value = ForgotPasswordUiState(isDataValid = true)
        }
    }
}