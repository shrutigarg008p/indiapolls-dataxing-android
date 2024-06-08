package com.dataxing.indiapolls.ui.register

import android.annotation.SuppressLint
import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.ui.otp.CompletionNavigation
import com.dataxing.indiapolls.ui.otp.OtpArgs
import com.dataxing.indiapolls.ui.theme.DefaultButton
import com.dataxing.indiapolls.ui.theme.DefaultOutlinedTextField
import com.dataxing.indiapolls.ui.theme.DefaultPasswordOutlinedTextField
import com.dataxing.indiapolls.ui.theme.defaultHeaderTextView
import com.dataxing.indiapolls.ui.theme.defaultSubHeaderTextView
import com.dataxing.indiapolls.ui.theme.errorString
import com.dataxing.indiapolls.ui.theme.hasValue
import com.dataxing.indiapolls.ui.theme.primary
import com.dataxing.indiapolls.ui.theme.white
import com.dataxing.indiapolls.ui.util.showMessage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(context: Context, navController: NavController, viewModel: RegisterViewModel) {
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var termsChecked by remember { mutableStateOf(false) }

    val loginUiState by viewModel.registerFormState.collectAsState()
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
                        val model = (result as Result.Success<RegisteredUserView>).data
                        context.showMessage(model.message)

                        val args = OtpArgs(mobile, CompletionNavigation.Login, model.userId)
                        val action = RegisterFragmentDirections.actionRegisterFragmentToVerifyOtpFragment2(args)
                        navController.navigate(action)

                        viewModel.reInitializeResult()
                    }
                    is Result.Error -> {
                        context.showMessage((result as Result.Error).exception)
                        
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
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.register_header),
                        style = defaultHeaderTextView,
                        modifier = Modifier.padding(top = 20.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.register_sub_header),
                        style = defaultSubHeaderTextView,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    DefaultOutlinedTextField(
                        30.dp,
                        text = email,
                        onValueChange = { email = it },
                        label = stringResource(id = R.string.prompt_your_email),
                        isError = loginUiState.emailError.hasValue,
                        singleLine = true,
                        errorText = loginUiState.emailError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )

                    DefaultOutlinedTextField(
                        16.dp,
                        text = mobile,
                        onValueChange = { mobile = it },
                        label = stringResource(id = R.string.prompt_mobile),
                        isError = loginUiState.emailError.hasValue,
                        singleLine = true,
                        errorText = loginUiState.emailError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
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

                    DefaultPasswordOutlinedTextField(
                        16.dp,
                        text = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = stringResource(id = R.string.prompt_confirm_password),
                        isError = loginUiState.passwordError.hasValue,
                        singleLine = true,
                        errorText = loginUiState.passwordError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Checkbox(
                            checked = termsChecked,
                            onCheckedChange = { termsChecked = it },
                            colors = CheckboxDefaults.colors(checkedColor = primary),
                            modifier = Modifier.padding(end = 8.dp)
                        )

                        AndroidView(
                            factory = { context ->
                                val view = LayoutInflater.from(context).inflate(R.layout.sign_up_term_condition_text_view, null).apply {
                                    layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )
                                }

                                (view as TextView).movementMethod = LinkMovementMethod.getInstance()

                                view
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    DefaultButton(
                        64.dp,
                        64.dp,
                        stringResource(id = R.string.next)
                    ) {
                        viewModel.register(email, mobile, password, confirmPassword, termsChecked)
                    }
                }
            }
        }
    )
}