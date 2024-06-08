package com.dataxing.indiapolls.ui.survey

data class SurveyItemView(
    val title: String,
    val time: Long?,
    val color: String,
    val url: String,
    val values: List<String>
)