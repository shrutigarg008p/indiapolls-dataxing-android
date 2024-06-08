package com.dataxing.indiapolls.ui.profile


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.ui.otp.CompletionNavigation
import com.dataxing.indiapolls.ui.otp.OtpArgs
import com.dataxing.indiapolls.ui.theme.DefaultButton
import com.dataxing.indiapolls.ui.theme.defaultHeaderTextView
import com.dataxing.indiapolls.ui.theme.defaultMediumTextView
import com.dataxing.indiapolls.ui.theme.deleteAccount
import com.dataxing.indiapolls.ui.theme.primary
import com.dataxing.indiapolls.ui.theme.white
import com.dataxing.indiapolls.ui.util.showMessage

@OptIn(ExperimentalGlideComposeApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    context: Context,
    navController: NavController,
    onProfileImageClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: ProfileViewModel
) {
    var imageUrl by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val result by viewModel.result.collectAsState()
    val uploadPictureResult by viewModel.uploadPictureResult.collectAsState()
    val sendOtpResult by viewModel.sendOtp.collectAsState()
    val unsubscribeResult by viewModel.unsubscribeResult.collectAsState()
    val deleteAccountResult by viewModel.deleteAccountResult.collectAsState()

    Scaffold(
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = white
            ) {
                when(result) {
                    is Result.Success -> {
                        val data = (result as Result.Success<ProfileView>).data
                        imageUrl = data.imageUrl.toString()
                        userName = data.name
                        email = data.email
                    }
                    is Result.Error -> context.showMessage((result as Result.Error).exception)
                    is Result.initialized -> {}
                }

                when(uploadPictureResult) {
                    is Result.Success -> {
                        val data = (uploadPictureResult as Result.Success<String>).data
                        imageUrl = data

                        viewModel.reInitializeUploadPictureResult()
                    }
                    is Result.Error -> context.showMessage((uploadPictureResult as Result.Error).exception)
                    is Result.initialized -> {}
                }

                when(sendOtpResult) {
                    is Result.Success -> {
                        (result as? Result.Success<ProfileView>)?.let {
                            val args = OtpArgs(it.data.mobileNumber, CompletionNavigation.Back)
                            val action = ProfileFragmentDirections.actionProfileFragmentToVerifyOtp(args)
                            navController.navigate(action)
                        }

                        viewModel.reInitializeOtpResult()
                    }
                    is Result.Error -> context.showMessage((sendOtpResult as Result.Error).exception)
                    is Result.initialized -> {}
                }

                when(unsubscribeResult) {
                    is Result.Success -> {
                        val data = (unsubscribeResult as Result.Success<String>).data
                        context.showMessage(data)

                        viewModel.reInitializeUnsubscribeResult()
                    }
                    is Result.Error -> context.showMessage((unsubscribeResult as Result.Error).exception)
                    is Result.initialized -> {}
                }

                when(deleteAccountResult) {
                    is Result.Success -> {
                        val data = (deleteAccountResult as Result.Success<String>).data
                        context.showMessage(data)

                        viewModel.reInitializeDeleteAccountResult()
                    }
                    is Result.Error -> context.showMessage((deleteAccountResult as Result.Error).exception)
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
                        .absolutePadding(bottom = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.my_profile),
                        style = defaultHeaderTextView,
                        modifier = Modifier.padding(top = 40.dp, bottom = 40.dp)
                    )

                    GlideImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                onProfileImageClick()
                            })
                            .width(IntrinsicSize.Min),
                        failure = placeholder(R.drawable.ic_avatar_placeholder)
                    )

                    Text(
                        text = userName,
                        style = defaultMediumTextView.copy(fontSize = 20.sp),
                        modifier = Modifier.padding(top = 12.dp, start = 24.dp, end = 24.dp)
                    )

                    Text(
                        text = email,
                        style = defaultMediumTextView.copy(fontSize = 14.sp),
                        modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp)
                    )

                    Box(modifier = Modifier.fillMaxSize().padding(top = 26.dp)) {
                        AndroidView(
                            factory = { context ->
                                LayoutInflater.from(context).inflate(R.layout.default_gradient_divider, null).apply {
                                    layoutParams = ViewGroup.LayoutParams(
                                        MATCH_PARENT,
                                        WRAP_CONTENT
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxSize().height(1.dp)
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.verify_mobile),
                        style = defaultMediumTextView,
                        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 24.dp, end = 24.dp)
                            .align(Alignment.Start)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                            viewModel.sendOtp()
                        })
                    )

                    Box(modifier = Modifier.fillMaxSize()) {
                        AndroidView(
                            factory = { context ->
                                LayoutInflater.from(context).inflate(R.layout.default_gradient_divider, null).apply {
                                    layoutParams = ViewGroup.LayoutParams(
                                        MATCH_PARENT,
                                        WRAP_CONTENT
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxSize().height(1.dp)
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.change_password),
                        style = defaultMediumTextView,
                        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 24.dp, end = 24.dp)
                            .align(Alignment.Start)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                navController.navigate(R.id.action_profileFragment_to_changePassword)
                            })
                    )

                    Box(modifier = Modifier.fillMaxSize()) {
                        AndroidView(
                            factory = { context ->
                                LayoutInflater.from(context).inflate(R.layout.default_gradient_divider, null).apply {
                                    layoutParams = ViewGroup.LayoutParams(
                                        MATCH_PARENT,
                                        WRAP_CONTENT
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxSize().height(1.dp)
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.unsubscribe_email),
                        style = defaultMediumTextView,
                        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 24.dp, end = 24.dp)
                            .align(Alignment.Start)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                viewModel.unsubscribeUser()
                            })
                    )

                    Box(modifier = Modifier.fillMaxSize()) {
                        AndroidView(
                            factory = { context ->
                                LayoutInflater.from(context).inflate(R.layout.default_gradient_divider, null).apply {
                                    layoutParams = ViewGroup.LayoutParams(
                                        MATCH_PARENT,
                                        WRAP_CONTENT
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxSize().height(1.dp)
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.delete_account),
                        style = defaultMediumTextView.copy(color = deleteAccount),
                        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 24.dp, end = 24.dp)
                            .align(Alignment.Start)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                viewModel.deleteAccount()
                            })
                    )

                    Box(modifier = Modifier.fillMaxSize()) {
                        AndroidView(
                            factory = { context ->
                                LayoutInflater.from(context).inflate(R.layout.default_gradient_divider, null).apply {
                                    layoutParams = ViewGroup.LayoutParams(
                                        MATCH_PARENT,
                                        WRAP_CONTENT
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxSize().height(1.dp)
                        )
                    }

                    DefaultButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp, bottom = 64.dp, start = 24.dp, end = 24.dp)
                            .height(52.dp),
                        stringResource(id = R.string.logout)
                    ) {
                        onLogoutClick()
                    }
                }
            }
        }
    )
}

