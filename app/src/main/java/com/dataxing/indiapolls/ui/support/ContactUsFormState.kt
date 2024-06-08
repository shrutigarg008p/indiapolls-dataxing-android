package com.dataxing.indiapolls.ui.support

data class ContactUsFormState(
    val queryError: Int? = null,
    val subjectError: Int? = null,
    val isDataValid: Boolean = false
)