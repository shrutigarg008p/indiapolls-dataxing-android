package com.dataxing.indiapolls.ui.otp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.ui.theme.DefaultButton
import com.dataxing.indiapolls.ui.theme.defaultHeaderTextView
import com.dataxing.indiapolls.ui.theme.defaultMediumTextView
import com.dataxing.indiapolls.ui.theme.defaultSubHeaderTextView
import com.dataxing.indiapolls.ui.theme.primary
import com.dataxing.indiapolls.ui.theme.white
import com.dataxing.indiapolls.ui.util.showMessage
import com.poovam.pinedittextfield.CirclePinField
import com.poovam.pinedittextfield.PinField

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VerifyOtpScreen(
    context: Context,
    activity: Activity?,
    navController: NavController,
    userId: String?,
    mobileNumber: String,
    completionNavigation: CompletionNavigation?,
    viewModel: VerifyOtpViewModel
) {
    var otpText by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val result by viewModel.result.collectAsState()
    val resendOtpResult by viewModel.resendOtpResult.collectAsState()
    val isOnBoardingResult by viewModel.isOnBoardingResult.collectAsState()

    Scaffold(
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = white
            ) {
                when(result) {
                    is Result.Success -> {
                        val data = (result as Result.Success<String>).data
                        context.showMessage(data)
                        when (completionNavigation) {
                            CompletionNavigation.Login -> {
                                navController.navigate(R.id.action_verifyOtpFragment_to_loginFragment)
                            }

                            CompletionNavigation.Home -> {
                                viewModel.fetchProfile()
                            }

                            else -> {
                                navController.popBackStack()
                            }
                        }

                        viewModel.reInitializeResult()
                    }
                    is Result.Error -> {
                        context.showMessage((result as Result.Error).exception)

                        viewModel.reInitializeResult()
                    }
                    is Result.initialized -> {}
                }

                when(resendOtpResult) {
                    is Result.Success -> {
                        val data = (resendOtpResult as Result.Success<String>).data
                        context.showMessage(data)

                        viewModel.reInitializeResult()
                    }
                    is Result.Error -> {
                        context.showMessage((resendOtpResult as Result.Error).exception)

                        viewModel.reInitializeResult()
                    }
                    is Result.initialized -> {}
                }

                when(isOnBoardingResult) {
                    is Result.Success -> {
                        val data = (isOnBoardingResult as Result.Success<Boolean>).data
                        if (data) {
                            navController.navigate(R.id.homeActivity)
                        } else {
                            navController.navigate(R.id.onBoardingActivity)
                        }
                        activity?.finish()

                        viewModel.reInitializeResult()
                    }
                    is Result.Error -> {
                        context.showMessage((isOnBoardingResult as Result.Error).exception)

                        viewModel.reInitializeResult()
                    }
                    is Result.initialized -> {}
                }

                if (isLoading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(50.dp),
                            color = primary
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .absolutePadding(left = 24.dp, right = 24.dp, bottom = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.verify_otp),
                        style = defaultHeaderTextView,
                        modifier = Modifier.padding(top = 20.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.otp_verification_help_text),
                        style = defaultSubHeaderTextView,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    AndroidView(
                        factory = { context ->
                            val view = LayoutInflater.from(context).inflate(R.layout.default_pin_circle_text_field, null).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                            }

                            (view as CirclePinField).onTextCompleteListener = object : PinField.OnTextCompleteListener {
                                override fun onTextComplete(enteredText: String): Boolean {
                                    otpText = enteredText
                                    return true
                                }
                            }

                            view
                        },
                        modifier = Modifier.fillMaxSize().padding(top = 64.dp, bottom = 12.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.resend_code),
                        style = defaultMediumTextView,
                        modifier = Modifier.padding(top = 12.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    viewModel.resendOtp(userId, mobileNumber)
                            })
                    )

                    DefaultButton(
                        64.dp,
                        64.dp,
                        stringResource(id = R.string.submit)
                    ) {
                        if (otpText.isEmpty()) {
                            val message = context.resources.getString(R.string.invalid_otp)
                            context.showMessage(message)
                        } else {
                            viewModel.verifyOtp(userId, otpText)
                        }
                    }
                }
            }
        }
    )
}