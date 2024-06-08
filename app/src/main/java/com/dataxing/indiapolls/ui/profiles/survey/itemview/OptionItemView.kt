package com.dataxing.indiapolls.ui.profiles.survey.itemview

data class OptionItemView(
    val id: String,
    val questionId: String,
    val value: String,
    val hint: String,
    val displayOrder: Long,
    val isActive: Boolean,
    var isSelected: Boolean
)