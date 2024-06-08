package com.dataxing.indiapolls.ui.util

import com.dataxing.indiapolls.imageBaseUrl

fun String?.absoluteImageUrlPath(): String {
    return imageBaseUrl + this
}