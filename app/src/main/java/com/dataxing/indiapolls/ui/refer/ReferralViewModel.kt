package com.dataxing.indiapolls.ui.refer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.data.referral.ReferralRequestDto
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncApiResultWithUserInfo
import com.dataxing.indiapolls.network.map
import com.dataxing.indiapolls.ui.util.isEmailValid
import com.dataxing.indiapolls.ui.util.isMobileNumberValid
import com.dataxing.indiapolls.ui.util.isValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReferralViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _formState = MutableStateFlow(ReferralFormState())
    val formState: StateFlow<ReferralFormState> = _formState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _result = MutableStateFlow<Result<ReferralView>>(Result.initialized())
    val result: StateFlow<Result<ReferralView>> = _result.asStateFlow()

    fun refer(name: String, email: String, phoneNumber: String) {
        dataChanged(name, email, phoneNumber)
        if (!formState.value.isDataValid) {
            return
        }

        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val view = asyncApiResultWithUserInfo(userPreferencesRepository) {
                    val requestDto = ReferralRequestDto(it.id, name, email, phoneNumber)
                    RetrofitHelper.apiService.refer(requestDto)
                }.map {
                   ReferralView(it.message)
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

    private fun dataChanged(name: String, email: String, phoneNumber: String) {
        if (!name.isValid) {
            _formState.value = ReferralFormState(nameError = R.string.invalid_name)
        } else if (!email.isEmailValid) {
            _formState.value = ReferralFormState(emailError = R.string.invalid_email)
        } else if (!phoneNumber.isMobileNumberValid) {
            _formState.value = ReferralFormState(mobileError = R.string.invalid_mobile_number)
        } else {
            _formState.value = ReferralFormState(isDataValid = true)
        }
    }
}