package com.dataxing.indiapolls.ui.profiles.survey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.data.profiles.Question
import com.dataxing.indiapolls.data.profiles.SubmitAnswersRequestDto
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncApiResultWithUserInfo
import com.dataxing.indiapolls.network.asyncResultWithUserInfo
import com.dataxing.indiapolls.network.map
import com.dataxing.indiapolls.network.toMessageResult
import com.dataxing.indiapolls.ui.profiles.survey.itemview.OptionItemView
import com.dataxing.indiapolls.ui.profiles.survey.itemview.SurveyQuestionItemView
import kotlinx.coroutines.launch

class ProfilesSurveyViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _result = MutableLiveData<Result<List<SurveyQuestionItemView>>>()
    val result: LiveData<Result<List<SurveyQuestionItemView>>> = _result

    private val _submitAnswerResult = MutableLiveData<Result<String>>()
    val submitAnswerResult: LiveData<Result<String>> = _submitAnswerResult

    private lateinit var profileId: String

    fun fetchProfilesSurveyQuestions(id: String) {
        profileId = id
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
               val view =  asyncResultWithUserInfo(userPreferencesRepository) {
                    RetrofitHelper.apiService.getProfilesSurveyQuestions(id, it.id)
                }.map { data ->
                    val items = data.questions.sortedBy { it.displayOrder }.map {
                        SurveyQuestionItemView(
                            it.id,
                            it.profileId,
                            it.text,
                            it.hint,
                            it.questionId,
                            it.displayOrder,
                            it.isActive,
                            it.displayType.toInt(),
                            it.dataType,
                            createOptionItems(it, data.response.response)
                        )
                    }.toMutableList()
                    val completionItem = items.last().copy(displayType = -1, options = mutableListOf(
                        OptionItemView("", "", "", "", 0, true, false)
                    ))
                    items.add(completionItem)

                    return@map items
                }

                _result.postValue(view)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun createOptionItems(question: Question, existingResponse: Map<String, Any>?): List<OptionItemView> {
        val selectedAnswer = existingResponse?.get(question.id)
        val items: List<OptionItemView>
        when(question.displayType.toInt()) {
            4 -> {
                val answers = selectedAnswer as? List<String>
                items = question.options.map {
                    OptionItemView(
                        it.id,
                        it.questionId,
                        it.value,
                        it.hint,
                        it.displayOrder,
                        it.isActive,
                        answers?.contains(it.id) ?: false
                    )
                }
            }
            else -> {
                val answer = selectedAnswer as? String
                items = question.options.map {
                    OptionItemView(
                        it.id,
                        it.questionId,
                        it.value,
                        it.hint,
                        it.displayOrder,
                        it.isActive,
                        answer == it.id
                    )
                }
            }
        }

        return items
    }

    fun updateItem(item: SurveyQuestionItemView) {
        result.value?.dataOrNull()?.let { data ->
            if (item.displayType >= 0) {
                data.indexOfFirst { it.id == item.id }.let {
                    if (it >= 0) {
                        val items = data.toMutableList()
                        items[it] = item
                        _result.postValue(Result.Success(items))
                    }
                }
            } else if (item.displayType == -1) {
                item.options.firstOrNull()?.let {
                    if (it.isSelected) {
                        submitAnswer(data)
                    }
                }
            }
        }
    }

    private fun submitAnswer(items: List<SurveyQuestionItemView>) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
               val view =  asyncApiResultWithUserInfo(userPreferencesRepository) {
                    val map = mutableMapOf<String, Any>()
                    items.forEach { item ->
                        when (item.displayType) {
                            4 -> {
                                val answers = item.options.filter { option ->
                                    option.isSelected
                                }.map { option ->
                                    option.id
                                }
                                if (answers.isNotEmpty()) {
                                    map[item.id] = answers
                                }
                            }

                            else -> {
                                item.options.firstOrNull { option ->
                                    option.isSelected
                                }?.let { option ->
                                    map[item.id] = option.id
                                }
                            }
                        }
                    }

                    val requestDto = SubmitAnswersRequestDto(it.id, profileId, map)
                    RetrofitHelper.apiService.submitProfilesSurveyQuestions(requestDto)
                }.toMessageResult()

                _submitAnswerResult.postValue(view)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}