package com.dataxing.indiapolls.ui.profiles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncResultWithUserInfo
import com.dataxing.indiapolls.network.map
import com.dataxing.indiapolls.ui.util.absoluteImageUrlPath
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfilesViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _result = MutableStateFlow<Result<List<ProfilesItemView>>>(Result.initialized())
    val result: StateFlow<Result<List<ProfilesItemView>>> = _result.asStateFlow()

    fun fetchProfiles() {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val result = asyncResultWithUserInfo(userPreferencesRepository) {
                    RetrofitHelper.apiService.getProfiles(it.id)
                }.map {
                    it.result.map { item ->ProfilesItemView(item.id, item.name, item.hindi, item.image.absoluteImageUrlPath(), item.attemptedQuestions > 0) }
                }
                _result.update { result }
            } finally {
                _isLoading.update { false }
            }
        }
    }
}