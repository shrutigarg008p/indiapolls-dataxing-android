package com.dataxing.indiapolls.ui.survey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncResultWithUserInfo
import com.dataxing.indiapolls.network.map
import com.dataxing.indiapolls.placeHolderColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SurveyViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _result = MutableStateFlow<Result<List<SurveyItemView>>>(Result.initialized())
    val result: StateFlow<Result<List<SurveyItemView>>> = _result.asStateFlow()

    private val profilePendingMessageColorCode = "#FF0000"

    private val _dashboardMessageUiState = MutableStateFlow<Result<DashboardMessageUiState>>(Result.initialized())
    val dashboardMessageUiState: StateFlow<Result<DashboardMessageUiState>> = _dashboardMessageUiState.asStateFlow()

    fun fetchSurvey() {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
               val view = asyncResultWithUserInfo(userPreferencesRepository) {
                    RetrofitHelper.apiService.getSurveys(it.id)
                }.map { dto ->
                   return@map dto.map {
                       val descriptions = mutableListOf<String>()
                       if (it.survey.descriptionOne.isNotEmpty()) {
                           descriptions.add(it.survey.descriptionOne)
                       }
                       if (it.survey.descriptionTwo.isNotEmpty()) {
                           descriptions.add(it.survey.descriptionTwo)
                       }
                       if (it.survey.descriptionThree.isNotEmpty()) {
                           descriptions.add(it.survey.descriptionThree)
                       }
                       if (it.survey.descriptionFour.isNotEmpty()) {
                           descriptions.add(it.survey.descriptionFour)
                       }

                       val color = if (it.survey.colorcode.isNullOrEmpty()) {
                           placeHolderColor
                       } else {
                           it.survey.colorcode
                       }
                       SurveyItemView(it.survey.name, it.survey.surveyLength, color, it.temporarySurveyLink, descriptions)
                   }.toMutableList()
               }

                _result.update {
                    Result.Success(
                        mutableListOf(
                            SurveyItemView(
                                "Profile",
                                4,
                                placeHolderColor,
                                "",
                                mutableListOf(
                                    "Tetsing Tetsing Tetsing Tetsing Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing"
                                )
                            ),
                            SurveyItemView(
                                "Profile",
                                3,
                                placeHolderColor,
                                "",
                                mutableListOf(
                                    "Tetsing Tetsing Tetsing Tetsing Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing"
                                )
                            ),
                            SurveyItemView(
                                "Profile",
                                12,
                                placeHolderColor,
                                "",
                                mutableListOf(
                                    "Tetsing Tetsing Tetsing Tetsing Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing"
                                )
                            ),
                            SurveyItemView(
                                "Profile",
                                11,
                                placeHolderColor,
                                "",
                                mutableListOf(
                                    "Tetsing Tetsing Tetsing Tetsing Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing"
                                )
                            ),
                            SurveyItemView(
                                "Profile",
                                20,
                                placeHolderColor,
                                "",
                                mutableListOf(
                                    "Tetsing Tetsing Tetsing Tetsing Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing"
                                )
                            ),
                            SurveyItemView(
                                "Profile",
                                18,
                                placeHolderColor,
                                "",
                                mutableListOf(
                                    "Tetsing Tetsing Tetsing Tetsing Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing",
                                    "Tetsing"
                                )
                            )
                        )
                    )
                }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun fetchProfile() {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val dashBoardUiState = asyncResultWithUserInfo(userPreferencesRepository) {
                    RetrofitHelper.apiService.getUser(it.id)
                }.map {
                    it.dashboardMessage.let { dashboardMessage ->
                        val colorCode = dashboardMessage?.colourCode.toString()
                        DashboardMessageUiState(
                            dashboardMessage?.messages.toString(),
                            colorCode,
                            colorCode == profilePendingMessageColorCode
                        )
                    }
                }

                _dashboardMessageUiState.update { dashBoardUiState }

            }  finally {
                _isLoading.update { false }
            }
        }
    }
}