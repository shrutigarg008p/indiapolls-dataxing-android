package com.dataxing.indiapolls.data.address

import com.dataxing.indiapolls.data.address.CityDto
import com.dataxing.indiapolls.data.address.StateDto

data class ZipCodeResponseDto(
    val cities: List<CityDto>,
    val state: List<StateDto>,
)