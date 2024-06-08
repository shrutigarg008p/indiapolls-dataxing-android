package com.dataxing.indiapolls.ui.onboarding.register

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.format.DateFormat
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
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
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.ui.onboarding.OnBoardingViewModel
import com.dataxing.indiapolls.ui.theme.DefaultButton
import com.dataxing.indiapolls.ui.theme.DefaultDropdownMenu
import com.dataxing.indiapolls.ui.theme.DefaultOutlinedTextField
import com.dataxing.indiapolls.ui.theme.DefaultOutlinedTextFieldWithIcon
import com.dataxing.indiapolls.ui.theme.defaultHeaderTextView
import com.dataxing.indiapolls.ui.theme.defaultSubHeaderTextView
import com.dataxing.indiapolls.ui.theme.errorString
import com.dataxing.indiapolls.ui.theme.hasValue
import com.dataxing.indiapolls.ui.theme.primary
import com.dataxing.indiapolls.ui.theme.white
import com.dataxing.indiapolls.ui.util.showMessage
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OnBoardingBasicInformationScreen(
    context: Context,
    activity: Activity?,
    parentFragmentManager: FragmentManager,
    navController: NavController,
    genders: List<String>,
    viewModel: OnBoardingViewModel
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }

    val uiState by viewModel.basicInformationState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val result by viewModel.result.collectAsState()

    val interactionSource = remember {
        object : MutableInteractionSource {
            override val interactions = MutableSharedFlow<Interaction>(
                extraBufferCapacity = 16,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )

            override suspend fun emit(interaction: Interaction) {
                if (interaction is PressInteraction.Release) {
                    val datePicker =
                        MaterialDatePicker.Builder.datePicker()
                            .setTitleText(R.string.select_date)
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .build()

                    datePicker.addOnPositiveButtonClickListener {
                        val value = DateFormat.format("dd/MM/yyyy", Date(it)).toString()
                        dateOfBirth = value
                    }
                    datePicker.show(parentFragmentManager, "Date picker")
                }
                interactions.emit(interaction)
            }

            override fun tryEmit(interaction: Interaction): Boolean {
                return interactions.tryEmit(interaction)
            }
        }
    }

    Scaffold(
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = white
            ) {
                when(result) {
                    is Result.Success -> {
                        navController.navigate(R.id.homeActivity3)
                        activity?.finish()

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
                        16.dp,
                        text = firstName,
                        onValueChange = { firstName = it },
                        label = stringResource(id = R.string.prompt_first_name),
                        isError = uiState.firstNameError.hasValue,
                        singleLine = true,
                        errorText = uiState.firstNameError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    DefaultOutlinedTextField(
                        16.dp,
                        text = lastName,
                        onValueChange = { lastName = it },
                        label = stringResource(id = R.string.prompt_last_name),
                        isError = uiState.lastNameError.hasValue,
                        singleLine = true,
                        errorText = uiState.lastNameError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    DefaultOutlinedTextField(
                        16.dp,
                        text = mobile,
                        onValueChange = { mobile = it },
                        label = stringResource(id = R.string.prompt_mobile),
                        isError = uiState.mobileNumberError.hasValue,
                        singleLine = true,
                        errorText = uiState.mobileNumberError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        )
                    )

                    DefaultDropdownMenu(
                        16.dp,
                        text = gender,
                        onValueChange = { _, item ->
                            gender = item
                        },
                        label = stringResource(id = R.string.gender),
                        isError = uiState.genderError.hasValue,
                        singleLine = true,
                        errorText = uiState.genderError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        options = genders
                    )

                    DefaultOutlinedTextFieldWithIcon(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        text = dateOfBirth,
                        onValueChange = { dateOfBirth = it },
                        label = stringResource(id = R.string.prompt_date_of_birth),
                        readOnly = true,
                        interactionSource = interactionSource,
                        isError = uiState.dateOfBirthError.hasValue,
                        singleLine = true,
                        errorText = uiState.dateOfBirthError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        trailingImage = Icons.Outlined.CalendarToday
                    ) {
                        val datePicker =
                            MaterialDatePicker.Builder.datePicker()
                                .setTitleText(R.string.select_date)
                                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                                .build()

                        datePicker.addOnPositiveButtonClickListener {
                            val value = DateFormat.format("dd/MM/yyyy", Date(it)).toString()
                            dateOfBirth = value
                        }

                        datePicker.show(parentFragmentManager, "Date picker")
                    }

                    DefaultButton(
                        64.dp,
                        64.dp,
                        stringResource(id = R.string.next)
                    ) {
                        viewModel.basicInformationDataChanged(firstName, lastName, mobile, gender, dateOfBirth)
                        if (uiState.isDataValid) {
                            viewModel.addBasicInformation(
                                firstName,
                                lastName,
                                mobile,
                                gender,
                                dateOfBirth
                            )

                            navController.navigate(R.id.action_onBoardingRegisterFragment_to_onBoardingAddressFragment)
                        }
                    }
                }
            }
        }
    )
}