package com.dataxing.indiapolls.data.contactus

data class ContactUsResponseDto (
    val createdAt: String,
    val updatedAt: String,
    val id: String,
    val userId: String,
    val queryType: String,
    val subject: String,
    val body: String,
    val queryStatus: String,
    val deletedAt: Any?,
)