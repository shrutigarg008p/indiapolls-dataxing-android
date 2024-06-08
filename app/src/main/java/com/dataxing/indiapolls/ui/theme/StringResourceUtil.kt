package com.dataxing.indiapolls.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

val Int?.hasValue: Boolean
        get() = this != null

val Int?.errorString: String
        @Composable
        get() = this?.let { stringResource(it) } ?: ""