package com.dataxing.indiapolls.data.address

data class CountryDto (
    val id: String,
    val stateId: String,
    val zipCode: String,
    val region: String,
    val segment: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
    val tier: Long,
)