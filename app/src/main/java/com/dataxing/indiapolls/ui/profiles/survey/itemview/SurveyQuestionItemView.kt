package com.dataxing.indiapolls.ui.profiles.survey.itemview

data class SurveyQuestionItemView(
    val id: String,
    val profileId: String,
    val text: String,
    val hint: String,
    val questionId: Long?,
    val displayOrder: Long,
    val isActive: Boolean,
    val displayType: Int,
    val dataType: Long,
    val options: List<OptionItemView>,
)