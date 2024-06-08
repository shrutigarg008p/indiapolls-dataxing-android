package com.dataxing.indiapolls.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.ui.otp.CompletionNavigation
import com.dataxing.indiapolls.ui.otp.OtpArgs
import com.dataxing.indiapolls.ui.theme.DefaultButton
import com.dataxing.indiapolls.ui.theme.DefaultIconButton
import com.dataxing.indiapolls.ui.theme.DefaultOutlinedTextField
import com.dataxing.indiapolls.ui.theme.DefaultPasswordOutlinedTextField
import com.dataxing.indiapolls.ui.theme.defaultHeaderTextView
import com.dataxing.indiapolls.ui.theme.defaultMediumTextView
import com.dataxing.indiapolls.ui.theme.errorString
import com.dataxing.indiapolls.ui.theme.facebook
import com.dataxing.indiapolls.ui.theme.hasValue
import com.dataxing.indiapolls.ui.theme.primary
import com.dataxing.indiapolls.ui.theme.textPrimary
import com.dataxing.indiapolls.ui.theme.white
import com.dataxing.indiapolls.ui.util.showMessage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(context: Context, activity: Activity?, navController: NavController, viewModel: LoginViewModel, onFacebookButtonClick: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginUiState by viewModel.formState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val result by viewModel.result.collectAsState()

    Scaffold(
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = white
            ) {
                when(result) {
                    is Result.Success -> {
                        val model = (result as Result.Success<LoggedInUserView>).data
                        if (model.isPhoneNumberConfirmed) {
                            if (model.isOnBoardingCompleted) {
                                navController.navigate(R.id.homeActivity)
                            } else {
                                navController.navigate(R.id.onBoardingActivity)
                            }
                            activity?.finish()
                        } else {
                            val args = OtpArgs(model.mobileNumber, CompletionNavigation.Home)
                            val action = LoginFragmentDirections.actionLoginFragmentToVerifyOtpFragment(args)
                            navController.navigate(action)
                        }

                        viewModel.reInitializeResult()
                    }
                    is Result.Error -> context.showMessage((result as Result.Error).exception)
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
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.login_header),
                        style = defaultHeaderTextView,
                        modifier = Modifier.padding(top = 40.dp)
                    )

                    DefaultOutlinedTextField(
                        40.dp,
                        text = username,
                        onValueChange = { username = it },
                        label = stringResource(id = R.string.prompt_your_email),
                        isError = loginUiState.emailError.hasValue,
                        singleLine = true,
                        errorText = loginUiState.emailError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    DefaultPasswordOutlinedTextField(
                        16.dp,
                        text = password,
                        onValueChange = { password = it },
                        label = stringResource(id = R.string.prompt_password),
                        isError = loginUiState.passwordError.hasValue,
                        singleLine = true,
                        errorText = loginUiState.passwordError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        ClickableText(
                            onClick = { navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment) },
                            style = defaultMediumTextView.copy(textAlign = TextAlign.End),
                            text = AnnotatedString(stringResource(id = R.string.forgot_password_register)),
                        )
                    }
                    
                    DefaultButton(
                        64.dp,
                        0.dp,
                        stringResource(id = R.string.login)
                    ) {
                        viewModel.login(username, password)
                    }

                    Text(
                        style = defaultMediumTextView,
                        text = stringResource(id = R.string.or),
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    DefaultButton(
                        16.dp,
                        0.dp,
                        stringResource(id = R.string.login_with_otp)
                    ){
                        navController.navigate(R.id.action_loginFragment_to_loginWithOtpFragment)
                        // Handle login action
                    }

                    Text(
                        style = defaultMediumTextView,
                        text = stringResource(id = R.string.or),
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    DefaultIconButton(
                        16.dp,
                        0.dp,
                        ButtonDefaults.buttonColors(containerColor = facebook),
                        R.drawable.ic_facebook,
                        white,
                        stringResource(id = R.string.connect_with_facebook)
                    ) {
                        onFacebookButtonClick()
                    }

                    val annotatedText = buildAnnotatedString {
                        append(stringResource(id = R.string.do_not_have_an_account))
                        withStyle(
                            style = SpanStyle(
                                color = primary,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            pushStringAnnotation("SignUp", "SignUp")
                            append(stringResource(id = R.string.sign_up))
                            pop()
                        }
                    }

                    ClickableText(
                        text = annotatedText,
                        onClick = { offset ->
                            annotatedText.getStringAnnotations(
                                tag = "SignUp", start = offset, end = offset
                            ).firstOrNull()?.let { annotation ->
                                navController.navigate(R.id.action_loginFragment_to_registerFragment)
                            }
                        },
                        style = defaultMediumTextView.copy(fontSize = 14.sp, color = textPrimary),
                        modifier = Modifier.padding(top = 30.dp, bottom = 64.dp)
                    )
                }
            }
        }
    )
}
