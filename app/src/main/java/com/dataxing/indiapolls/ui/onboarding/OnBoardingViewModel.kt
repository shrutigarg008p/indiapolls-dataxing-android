package com.dataxing.indiapolls.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.data.address.CityDto
import com.dataxing.indiapolls.data.address.CountryDto
import com.dataxing.indiapolls.data.address.StateDto
import com.dataxing.indiapolls.data.auth.login.UserInfo
import com.dataxing.indiapolls.data.auth.onboarding.OnBoardingRequestDto
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncApiResultWithUserInfo
import com.dataxing.indiapolls.network.asyncResult
import com.dataxing.indiapolls.network.map
import com.dataxing.indiapolls.network.toMessageResult
import com.dataxing.indiapolls.ui.onboarding.register.OnBoardingAddressFormState
import com.dataxing.indiapolls.ui.onboarding.register.OnBoardingBasicInformationFormState
import com.dataxing.indiapolls.ui.util.isMobileNumberValid
import com.dataxing.indiapolls.ui.util.isValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnBoardingViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    private val _basicInformationState = MutableStateFlow(OnBoardingBasicInformationFormState())
    val basicInformationState: StateFlow<OnBoardingBasicInformationFormState> = _basicInformationState.asStateFlow()

    private val _addressFormState = MutableStateFlow(OnBoardingAddressFormState())
    val addressFormState: StateFlow<OnBoardingAddressFormState> = _addressFormState.asStateFlow()

    private val _request = MutableStateFlow(OnBoardingRequestDto())
    val request: StateFlow<OnBoardingRequestDto> = _request.asStateFlow()

    private val _countries = MutableStateFlow<Result<List<CountryDto>>>(Result.initialized())
    val countries: StateFlow<Result<List<CountryDto>>> = _countries

    private val _states = MutableStateFlow<Result<List<StateDto>>>(Result.initialized())
    val states: StateFlow<Result<List<StateDto>>> = _states.asStateFlow()

    private val _cities = MutableStateFlow<Result<List<CityDto>>>(Result.initialized())
    val cities: StateFlow<Result<List<CityDto>>> = _cities.asStateFlow()

    private val _result = MutableStateFlow<Result<String>>(Result.initialized())
    val result: StateFlow<Result<String>> = _result.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var requestDto = OnBoardingRequestDto()

    fun initialize() {
        initializeRequestDto()
        getCountries()
    }

    private fun initializeRequestDto() {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val info = userPreferencesRepository.fetchUserInformation()
                info?.let { userInfo ->
                    _request.update { OnBoardingRequestDto(mobile = userInfo.phoneNumber) }
                }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun getCountries() {
        if (countries.value == Result.initialized() || countries.value is Result.Error) {
            _isLoading.update { true }
            viewModelScope.launch {
                try {
                    val items = asyncResult {
                        RetrofitHelper.apiService.getCountries()
                    }
                    _countries.update { items }
                } finally {
                    _isLoading.update { false }
                }
            }
        }
    }

    fun getAllStatesAndCitiesByZipCode(zipCode: String) {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val result = asyncResult {
                    RetrofitHelper.apiService.getAllStatesAndCitiesByZipCode(zipCode)
                }
                val states = result.map {
                    it.state
                }
                _states.update { states }

                val cities = result.map {
                    it.cities
                }
                _cities.update { cities }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun clearStatesAndCities() {
        _states.update { Result.Success(listOf()) }
        _cities.update { Result.Success(listOf()) }
    }

    fun basicInformationDataChanged(firstName: String, lastName: String, mobile: String, gender: String, dateOfBirth: String) {
        if (!firstName.isValid) {
            _basicInformationState.value = OnBoardingBasicInformationFormState(firstNameError = R.string.invalid_first_name)
        } else if (!lastName.isValid) {
            _basicInformationState.value = OnBoardingBasicInformationFormState(lastNameError = R.string.invalid_last_name)
        } else if (!mobile.isMobileNumberValid) {
            _basicInformationState.value = OnBoardingBasicInformationFormState(mobileNumberError = R.string.invalid_mobile_number)
        } else if (!gender.isValid) {
            _basicInformationState.value = OnBoardingBasicInformationFormState(genderError = R.string.invalid_gender)
        } else if (!dateOfBirth.isValid) {
            _basicInformationState.value = OnBoardingBasicInformationFormState(dateOfBirthError = R.string.invalid_date_of_birth)
        } else {
            _basicInformationState.value = OnBoardingBasicInformationFormState(isDataValid = true)
        }
    }

    fun addBasicInformation(firstName: String, lastName: String, mobile: String, gender: String, dateOfBirth: String) {
        requestDto = OnBoardingRequestDto(firstName, lastName, mobile, gender, dateOfBirth)
        _request.update { requestDto }
    }

    fun addressInformationDataChanged(address1: String, country: String, state: String, city: String, referral: String, pinCode: String) {
        if (!address1.isValid) {
            _addressFormState.value = OnBoardingAddressFormState(addressError = R.string.invalid_address)
        } else if (!pinCode.isValid) {
            _addressFormState.value = OnBoardingAddressFormState(pinCodeError = R.string.invalid_pin_code)
        } else if (!country.isValid) {
            _addressFormState.value = OnBoardingAddressFormState(countryError = R.string.invalid_country)
        } else if (!state.isValid) {
            _addressFormState.value = OnBoardingAddressFormState(stateError = R.string.invalid_state)
        } else if (!city.isValid) {
            _addressFormState.value = OnBoardingAddressFormState(cityError = R.string.invalid_city)
        } else if (!referral.isValid) {
            _addressFormState.value = OnBoardingAddressFormState(referralCodeError = R.string.invalid_referral_code)
        } else {
            _addressFormState.value = OnBoardingAddressFormState(isDataValid = true)
        }
    }

    fun addAddress(address1: String, address2: String, country: String, state: String, city: String, referral: String, pinCode: String) {
        requestDto = requestDto.copy().apply {
            addressLine1 = address1
            addressLine2 = address2
            this.country = country
            this.state = state
            this.city = city
            referralSource = referral
            this.pinCode = pinCode
            acceptTerms = true
        }
        updateBasicProfile()
    }

    private fun updateBasicProfile() {
        _isLoading.update { true }
        viewModelScope.launch {
            var info: UserInfo? = null
            try {
                val result = asyncApiResultWithUserInfo(userPreferencesRepository) {
                    info = it
                    RetrofitHelper.apiService.updateBasicProfile(it.id, requestDto)
                }

                result.dataOrNull()?.let {
                    info?.let { info ->
                        info.basicProfile = it.data.basicProfile
                        userPreferencesRepository.updateUserInformation(info)
                    }
                }
                _result.update { result.toMessageResult() }

            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun reInitializeResult() {
        _result.update { Result.initialized() }
    }
}