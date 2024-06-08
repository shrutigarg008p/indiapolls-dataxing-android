package com.dataxing.indiapolls.ui.password.change

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
import com.dataxing.indiapolls.ui.theme.DefaultPasswordOutlinedTextField
import com.dataxing.indiapolls.ui.theme.defaultHeaderTextView
import com.dataxing.indiapolls.ui.theme.errorString
import com.dataxing.indiapolls.ui.theme.hasValue
import com.dataxing.indiapolls.ui.theme.primary
import com.dataxing.indiapolls.ui.theme.white
import com.dataxing.indiapolls.ui.util.showMessage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChangePasswordScreen(context: Context, navController: NavController, viewModel: ChangePasswordViewModel) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
                        val data = (result as Result.Success<ChangePasswordResult>).data
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
                        text = stringResource(id = R.string.change_password),
                        style = defaultHeaderTextView,
                        modifier = Modifier.padding(top = 20.dp)
                    )

                    DefaultPasswordOutlinedTextField(
                        40.dp,
                        text = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = stringResource(id = R.string.prompt_current_password),
                        isError = uiState.currentPasswordError.hasValue,
                        singleLine = true,
                        errorText = uiState.currentPasswordError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        )
                    )

                    DefaultPasswordOutlinedTextField(
                        16.dp,
                        text = newPassword,
                        onValueChange = { newPassword = it },
                        label = stringResource(id = R.string.prompt_new_password),
                        isError = uiState.currentPasswordError.hasValue,
                        singleLine = true,
                        errorText = uiState.currentPasswordError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        )
                    )

                    DefaultPasswordOutlinedTextField(
                        16.dp,
                        text = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = stringResource(id = R.string.prompt_confirm_new_password),
                        isError = uiState.currentPasswordError.hasValue,
                        singleLine = true,
                        errorText = uiState.currentPasswordError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        )
                    )

                    DefaultButton(
                        64.dp,
                        0.dp,
                        stringResource(id = R.string.submit)
                    ) {
                        viewModel.changePassword(currentPassword, newPassword, confirmPassword)
                    }
                }
            }
        }
    )
}
