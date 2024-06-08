package com.dataxing.indiapolls.ui.reward

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

class RewardViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _result = MutableStateFlow<Result<List<RewardItemView>>>(Result.initialized())
    val result: StateFlow<Result<List<RewardItemView>>> = _result

    fun fetchReward() {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val view = asyncResultWithUserInfo(userPreferencesRepository) {
                    RetrofitHelper.apiService.getRewards(it.id)
                }.map { dto ->
                    val items = dto.items.map {
                        RewardItemView(
                            it.survey?.name ?: it.rewardType,
                            (it.survey?.ceggPoints ?: it.points).toString()
                        )
                    }.toMutableList()

                    val totalPointItems = dto.totalPointsInfo.map {
                        RewardItemView(
                            it.name,
                            it.value.toString()
                        )
                    }.toMutableList()

                    return@map items + totalPointItems
                }
                _result.update { view }
            }  finally {
                _isLoading.update { false }
            }
        }
    }
}