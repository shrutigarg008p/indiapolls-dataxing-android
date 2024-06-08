package com.dataxing.indiapolls.ui.onboarding.register

import android.annotation.SuppressLint
import android.app.Activity
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
import com.dataxing.indiapolls.data.address.CityDto
import com.dataxing.indiapolls.data.address.CountryDto
import com.dataxing.indiapolls.data.address.StateDto
import com.dataxing.indiapolls.ui.onboarding.OnBoardingViewModel
import com.dataxing.indiapolls.ui.reward.redeem.RedemptionModeItemView
import com.dataxing.indiapolls.ui.theme.DefaultButton
import com.dataxing.indiapolls.ui.theme.DefaultDropdownMenu
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
fun OnBoardingAddressScreen(context: Context, activity: Activity?, navController: NavController, referrals: List<String>, viewModel: OnBoardingViewModel) {
    var address1 by remember { mutableStateOf("") }
    var address2 by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var referral by remember { mutableStateOf("") }

    val uiState by viewModel.addressFormState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val result by viewModel.result.collectAsState()

    val countriesState by viewModel.countries.collectAsState()
    var countries by remember { mutableStateOf<List<String>>(emptyList()) }

    val statesState by viewModel.states.collectAsState()
    var states by remember { mutableStateOf<List<String>>(emptyList()) }

    val citiesState by viewModel.cities.collectAsState()
    var cities by remember { mutableStateOf<List<String>>(emptyList()) }

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

                when(countriesState) {
                    is Result.Success -> {
                        val data = (countriesState as Result.Success<List<CountryDto>>).data
                        countries = data.map { it.name }
                    }
                    is Result.Error -> context.showMessage((countriesState as Result.Error).exception)
                    is Result.initialized -> {}
                }

                when(statesState) {
                    is Result.Success -> {
                        val data = (statesState as Result.Success<List<StateDto>>).data
                        states = data.map { it.name }
                    }
                    is Result.Error -> context.showMessage((statesState as Result.Error).exception)
                    is Result.initialized -> {}
                }

                when(citiesState) {
                    is Result.Success -> {
                        val data = (citiesState as Result.Success<List<CityDto>>).data
                        cities = data.map { it.name }
                    }
                    is Result.Error -> context.showMessage((citiesState as Result.Error).exception)
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
                        text = address1,
                        onValueChange = { address1 = it },
                        label = stringResource(id = R.string.address1),
                        isError = uiState.addressError.hasValue,
                        singleLine = true,
                        errorText = uiState.addressError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    DefaultOutlinedTextField(
                        16.dp,
                        text = address2,
                        onValueChange = { address2 = it },
                        label = stringResource(id = R.string.address2),
                        isError = false,
                        singleLine = true,
                        errorText = "",
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    DefaultOutlinedTextField(
                        16.dp,
                        text = zipCode,
                        onValueChange = {
                            zipCode = it
                            if (it.length > 2) {
                                viewModel.getAllStatesAndCitiesByZipCode(it)
                            } else {
                                viewModel.clearStatesAndCities()
                            }
                        },
                        label = stringResource(id = R.string.pin_code),
                        isError = uiState.pinCodeError.hasValue,
                        singleLine = true,
                        errorText = uiState.pinCodeError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    DefaultDropdownMenu(
                        16.dp,
                        text = country,
                        onValueChange = { _, item ->
                            country = item
                        },
                        onExpandedChange = {
                           if (it) {
                               viewModel.getCountries()
                           }
                        },
                        label = stringResource(id = R.string.country),
                        isError = uiState.countryError.hasValue,
                        singleLine = true,
                        errorText = uiState.countryError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        options = countries
                    )

                    DefaultDropdownMenu(
                        16.dp,
                        text = state,
                        onValueChange = { _, item ->
                            state = item
                        },
                        label = stringResource(id = R.string.state),
                        isError = uiState.stateError.hasValue,
                        singleLine = true,
                        errorText = uiState.stateError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        options = states
                    )

                    DefaultDropdownMenu(
                        16.dp,
                        text = city,
                        onValueChange = { _, item ->
                            city = item
                        },
                        label = stringResource(id = R.string.city_or_post_office),
                        isError = uiState.cityError.hasValue,
                        singleLine = true,
                        errorText = uiState.cityError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        options = cities
                    )

                    DefaultDropdownMenu(
                        16.dp,
                        text = referral,
                        onValueChange = { _, item ->
                            referral = item
                        },
                        label = stringResource(id = R.string.select_referral),
                        isError = uiState.referralCodeError.hasValue,
                        singleLine = true,
                        errorText = uiState.referralCodeError.errorString,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        options = referrals
                    )

                    DefaultButton(
                        64.dp,
                        64.dp,
                        stringResource(id = R.string.next)
                    ) {
                       viewModel.addressInformationDataChanged(address1, country, state, city, referral, zipCode)
                        if (uiState.isDataValid) {
                            viewModel.addAddress(address1, address2, country, state, city, referral, zipCode)
                        }
                    }
                }
            }
        }
    )
}