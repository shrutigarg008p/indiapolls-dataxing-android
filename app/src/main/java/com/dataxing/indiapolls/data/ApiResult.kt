package com.dataxing.indiapolls.data

data class ApiResult<out T : Any?>(
    val data: T,
    val status: Int,
    val message: String
)

