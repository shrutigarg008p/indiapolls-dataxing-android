package com.dataxing.indiapolls.data.address

data class CityDto (
    val id: String,
    val stateId: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
    val tier: Int,
)