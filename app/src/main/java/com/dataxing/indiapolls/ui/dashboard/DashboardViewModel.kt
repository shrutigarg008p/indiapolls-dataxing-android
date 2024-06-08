package com.dataxing.indiapolls.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncResultWithUserInfo
import com.dataxing.indiapolls.network.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _result = MutableStateFlow<Result<List<DashboardItemView>>>(Result.initialized())
    val result: StateFlow<Result<List<DashboardItemView>>> = _result.asStateFlow()

    fun fetchDashboard() {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val view = asyncResultWithUserInfo(userPreferencesRepository) {
                    RetrofitHelper.apiService.getDashboard(it.id)
                }.map { dto ->
                   return@map dto.map { DashboardItemView(it.name, it.points.toString()) }
                }
                _result.update { view }
            } catch (e: Exception) {
                _result.update { Result.Error(e) }
            } finally {
                _isLoading.update { false }
            }
        }
    }
}