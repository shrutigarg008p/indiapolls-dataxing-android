package com.dataxing.indiapolls.ui.password.change

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.data.password.ChangePasswordRequestDto
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncApiResultWithUserInfo
import com.dataxing.indiapolls.network.map
import com.dataxing.indiapolls.ui.util.isConfirmPasswordValid
import com.dataxing.indiapolls.ui.util.isValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePasswordViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _formState = MutableStateFlow(ChangePasswordFormState())
    val formState: StateFlow<ChangePasswordFormState> = _formState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _result = MutableStateFlow<Result<ChangePasswordResult>>(Result.initialized())
    val result: StateFlow<Result<ChangePasswordResult>> = _result.asStateFlow()

    fun changePassword(currentPassword: String, newPassword: String, confirmNewPassword: String) {
        dataChanged(currentPassword, newPassword, confirmNewPassword)
        if (!formState.value.isDataValid) {
            return
        }

        _isLoading.update {true }
        viewModelScope.launch {
            try {
                val view = asyncApiResultWithUserInfo(userPreferencesRepository) {
                    val requestDto = ChangePasswordRequestDto(it.id, currentPassword, newPassword)
                    RetrofitHelper.apiService.changePassword(requestDto)
                }.map {
                    ChangePasswordResult(it.message)
                }
                _result.update{ view }
            } finally {
                _isLoading.update {false }
            }
        }
    }

    fun reInitializeResult() {
        _result.update { Result.initialized() }
    }

    private  fun dataChanged(currentPassword: String, newPassword: String, confirmNewPassword: String) {
        if (!currentPassword.isValid) {
            _formState.value = ChangePasswordFormState(currentPasswordError = R.string.invalid_password)
        } else if (!newPassword.isValid) {
            _formState.value = ChangePasswordFormState(newPasswordError = R.string.invalid_password)
        } else if (!newPassword.isConfirmPasswordValid(confirmNewPassword)) {
            _formState.value = ChangePasswordFormState(newConfirmPasswordError = R.string.invalid_confirm_password)
        } else {
            _formState.value = ChangePasswordFormState(isDataValid = true)
        }
    }
}