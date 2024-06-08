package com.dataxing.indiapolls.ui.reward.redeem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.data.reward.RedeemPointsRequestDto
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncApiResultWithUserInfo
import com.dataxing.indiapolls.network.asyncResult
import com.dataxing.indiapolls.network.map
import com.dataxing.indiapolls.ui.util.isValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class RedeemPointsViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _formState = MutableStateFlow(RedeemPointsFormState())
    val formState: StateFlow<RedeemPointsFormState> = _formState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _result = MutableStateFlow<Result<String>>(Result.initialized())
    val result: StateFlow<Result<String>> = _result.asStateFlow()

    private val _redemptionModeResult = MutableStateFlow<Result<List<RedemptionModeItemView>>>(Result.initialized())
    val redemptionModeResult: StateFlow<Result<List<RedemptionModeItemView>>> = _redemptionModeResult.asStateFlow()

    fun fetchRedemptionModes() {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val view = asyncResult {
                    RetrofitHelper.apiService.getAllRedemptionModes()
                }.map { items ->
                    items.map { RedemptionModeItemView(it.id, it.name) }
                }
                _redemptionModeResult.update { view }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun redeemPoints(points: String, description: String?, redemptionModeName: String) {
        dataChanged(points, redemptionModeName)
        if (!formState.value.isDataValid) {
            return
        }

        val value = points.toLongOrNull() ?: 0
        (_redemptionModeResult.value as? Result.Success<List<RedemptionModeItemView>>)?.data?.let { items ->
            items.indexOfFirst { it.name == redemptionModeName }.let { index ->
                if (index >= 0) {
                    val redemptionModeItemView = items[index]

                    _isLoading.update { true }
                    viewModelScope.launch {
                        try {
                           val view = asyncApiResultWithUserInfo(userPreferencesRepository) {
                               val requestDto = RedeemPointsRequestDto(
                                   it.id,
                                   redemptionModeItemView.name,
                                   redemptionModeItemView.id,
                                   value,
                                   0,
                                   "New",
                                   Date().toString(),
                                   description
                               )
                               RetrofitHelper.apiService.redeemPoints(requestDto)
                           }.map {
                               it.message
                           }

                            _result.update { view }
                        } finally {
                            _isLoading.update { false }
                        }
                    }
                }
            }
        }
    }

    fun reInitializeResult() {
        _result.update { Result.initialized() }
    }

    private fun dataChanged(points: String, redemptionModeName: String) {
        if (!points.isValid) {
            _formState.value = RedeemPointsFormState(pointsError = R.string.invalid_points)
        } else if (!redemptionModeName.isValid) {
            _formState.value = RedeemPointsFormState(selectModeError = R.string.invalid_mode)
        } else {
            _formState.value = RedeemPointsFormState(isDataValid = true)
        }
    }
}