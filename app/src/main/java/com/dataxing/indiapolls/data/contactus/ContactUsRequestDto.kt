package com.dataxing.indiapolls.data.contactus

class ContactUsRequestDto(
    var userId: String,
    var queryType: String,
    var subject: String? = null,
    var body: String? = null,
    var queryStatus: String = "Pending"
)