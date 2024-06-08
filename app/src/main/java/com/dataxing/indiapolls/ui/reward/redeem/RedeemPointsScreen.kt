package com.dataxing.indiapolls.ui.reward.redeem

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
import com.dataxing.indiapolls.ui.theme.DefaultDropdownMenu
import com.dataxing.indiapolls.ui.theme.DefaultOutlinedTextField
import com.dataxing.indiapolls.ui.theme.defaultHeaderTextView
import com.dataxing.indiapolls.ui.theme.errorString
import com.dataxing.indiapolls.ui.theme.hasValue
import com.dataxing.indiapolls.ui.theme.primary
import com.dataxing.indiapolls.ui.theme.white
import com.dataxing.indiapolls.ui.util.showMessage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RedeemPointsScreen(context: Context, navController: NavController, viewModel: RedeemPointsViewModel) {
    var points by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("") }

    val uiState by viewModel.formState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val result by viewModel.result.collectAsState()
    val modesResult by viewModel.redemptionModeResult.collectAsState()
    var modes by remember { mutableStateOf<List<RedemptionModeItemView>>(emptyList()) }

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
                        navController.popBackStack()

                        viewModel.reInitializeResult()
                    }
                    is Result.Error -> {
                        context.showMessage((result as Result.Error).exception)

                        viewModel.reInitializeResult()
                    }
                    is Result.initialized -> {}
                }

                when(modesResult) {
                    is Result.Success -> {
                        val data = (modesResult as Result.Success<List<RedemptionModeItemView>>).data
                        modes = data
                    }
                    is Result.Error -> context.showMessage((modesResult as Result.Error).exception)
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
                        text = stringResource(id = R.string.redeem_points),
                        style = defaultHeaderTextView,
                        modifier = Modifier.padding(top = 20.dp)
                    )

                    DefaultOutlinedTextField(
                        40.dp,
                        text = points,
                        onValueChange = { points = it },
                        label = stringResource(id = R.string.points),
                        isError = uiState.pointsError.hasValue,
                        singleLine = true,
                        errorText = uiState.pointsError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        )
                    )

                    DefaultOutlinedTextField(
                        16.dp,
                        text = description,
                        onValueChange = { description = it },
                        label = stringResource(id = R.string.description),
                        isError = false,
                        singleLine = false,
                        minLines = 4,
                        maxLines = 4,
                        errorText = "",
                        keyboardOptions = KeyboardOptions.Default
                    )

                    DefaultDropdownMenu(
                        16.dp,
                        text = mode,
                        onValueChange = { _, item ->
                            mode = item
                        },
                        label = stringResource(id = R.string.select_mode),
                        isError = uiState.selectModeError.hasValue,
                        singleLine = true,
                        errorText = uiState.selectModeError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        options = modes.map { it.name }
                    )

                    DefaultButton(
                        64.dp,
                        64.dp,
                        stringResource(id = R.string.redeem_points)
                    ) {
                        viewModel.redeemPoints(points, description, mode)
                    }
                }
            }
        }
    )
}