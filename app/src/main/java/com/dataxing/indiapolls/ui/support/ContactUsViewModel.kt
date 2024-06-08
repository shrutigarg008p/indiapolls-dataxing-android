package com.dataxing.indiapolls.ui.support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.data.contactus.ContactUsRequestDto
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncApiResultWithUserInfo
import com.dataxing.indiapolls.network.map
import com.dataxing.indiapolls.ui.util.isValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactUsViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _formState = MutableStateFlow(ContactUsFormState())
    val formState: StateFlow<ContactUsFormState> = _formState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _result = MutableStateFlow<Result<ContactUsView>>(Result.initialized())
    val result: StateFlow<Result<ContactUsView>> = _result.asStateFlow()

    fun contactUs(queryType: String, subject: String, body: String? = null) {
        dataChanged(queryType, subject)
        if (!formState.value.isDataValid) {
            return
        }

        _isLoading.update{ true }
        viewModelScope.launch {
            try {
                val view = asyncApiResultWithUserInfo(userPreferencesRepository) {
                    val requestDto = ContactUsRequestDto(it.id, queryType, subject, body)
                    RetrofitHelper.apiService.contactUs(requestDto)
                }.map {
                    ContactUsView(it.message)
                }
                _result.update { view }
            } finally {
                _isLoading.update {false }
            }
        }
    }

    fun reInitializeResult() {
        _result.update { Result.initialized() }
    }

    private fun dataChanged(queryType: String, subject: String) {
        if (!queryType.isValid) {
            _formState.value = ContactUsFormState(queryError = R.string.invalid_query)
        } else if (!subject.isValid) {
            _formState.value = ContactUsFormState(subjectError = R.string.invalid_subject)
        } else {
            _formState.value = ContactUsFormState(isDataValid = true)
        }
    }
}