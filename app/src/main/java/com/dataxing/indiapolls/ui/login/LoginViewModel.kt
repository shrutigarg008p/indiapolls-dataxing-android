package com.dataxing.indiapolls.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.data.auth.login.LoginWithFacebookRequestDto
import com.dataxing.indiapolls.data.auth.login.LoginWithPasswordRequestDto
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncResult
import com.dataxing.indiapolls.network.map
import com.dataxing.indiapolls.ui.util.isEmailValid
import com.dataxing.indiapolls.ui.util.isValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _formState = MutableStateFlow(LoginFormState())
    val formState: StateFlow<LoginFormState> = _formState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _result = MutableStateFlow<Result<LoggedInUserView>>(Result.initialized())
    val result: StateFlow<Result<LoggedInUserView>> = _result

    fun login(email: String, password: String) {
        dataChanged(email, password)
        if (!formState.value.isDataValid) {
            return
        }

        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val result = asyncResult {
                    val request = LoginWithPasswordRequestDto(email, password, "password")
                    RetrofitHelper.apiService.loginWithPassword(request)
                }

                result.dataOrNull()?.let {
                    userPreferencesRepository.updateUserInformation(it)
                }

                val view = result.map {
                    LoggedInUserView(it.email.toString(), it.phoneNumber.toString(), it.basicProfile != null, it.phoneNumberConfirmed)
                }
                _result.update {  view }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun loginWithFacebook(email: String, udId: String, phoneNumber: String?) {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val result = asyncResult {
                    val request = LoginWithFacebookRequestDto(email, udId, "panelist", "facebook")
                    RetrofitHelper.apiService.loginWithFacebook(request)
                }

                result.dataOrNull()?.let {
                    val number = it.phoneNumber ?: phoneNumber
                    userPreferencesRepository.updateUserInformation(it.copy(phoneNumber = number))
                }

                val view = result.map {
                    LoggedInUserView(it.email.toString(), it.phoneNumber.toString(), it.basicProfile != null, true)
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

   private fun dataChanged(email: String, password: String) {
        if (!email.isEmailValid) {
            _formState.value = LoginFormState(emailError = R.string.invalid_email)
        } else if (!password.isValid) {
            _formState.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _formState.value = LoginFormState(isDataValid = true)
        }
    }
}