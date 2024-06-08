package com.dataxing.indiapolls.ui.refer

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.ui.theme.DefaultButton
import com.dataxing.indiapolls.ui.theme.DefaultOutlinedTextField
import com.dataxing.indiapolls.ui.theme.defaultHeaderTextView
import com.dataxing.indiapolls.ui.theme.defaultSubHeaderTextView
import com.dataxing.indiapolls.ui.theme.errorString
import com.dataxing.indiapolls.ui.theme.hasValue
import com.dataxing.indiapolls.ui.theme.primary
import com.dataxing.indiapolls.ui.theme.white
import com.dataxing.indiapolls.ui.util.showMessage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReferralScreen(context: Context, navController: NavController, viewModel: ReferralViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val uiState by viewModel.formState.collectAsState()
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
                        val data = (result as Result.Success<ReferralView>).data
                        context.showMessage(data.message)
                        navController.popBackStack()

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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.refer_a_friend),
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
                        text = name,
                        onValueChange = { name = it },
                        label = stringResource(id = R.string.prompt_enter_name),
                        isError = uiState.nameError.hasValue,
                        singleLine = true,
                        errorText = uiState.nameError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    DefaultOutlinedTextField(
                        16.dp,
                        text = email,
                        onValueChange = { email = it },
                        label = stringResource(id = R.string.prompt_enter_email),
                        isError = uiState.emailError.hasValue,
                        singleLine = true,
                        errorText = uiState.emailError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        )
                    )

                    DefaultOutlinedTextField(
                        16.dp,
                        text = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = stringResource(id = R.string.prompt_enter_mobile),
                        isError = uiState.mobileError.hasValue,
                        singleLine = true,
                        errorText = uiState.mobileError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Done
                        )
                    )

                    DefaultButton(
                        64.dp,
                        0.dp,
                        stringResource(id = R.string.refer)
                    ) {
                        viewModel.refer(name, email, phoneNumber)
                    }
                }
            }
        }
    )
}