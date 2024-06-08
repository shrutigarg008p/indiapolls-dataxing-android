package com.dataxing.indiapolls.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.data.auth.ResendOtpRequestDto
import com.dataxing.indiapolls.data.auth.login.UserInfo
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncApiResultWithUserInfo
import com.dataxing.indiapolls.network.asyncResultWithUserInfo
import com.dataxing.indiapolls.network.map
import com.dataxing.indiapolls.network.toMessageResult
import com.dataxing.indiapolls.ui.util.absoluteImageUrlPath
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProfileViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _result = MutableStateFlow<Result<ProfileView>>(Result.initialized())
    val result: StateFlow<Result<ProfileView>> = _result.asStateFlow()

    private val _uploadPictureResult = MutableStateFlow<Result<String>>(Result.initialized())
    val uploadPictureResult: StateFlow<Result<String>> = _uploadPictureResult.asStateFlow()

    private val _sendOtp = MutableStateFlow<Result<String>>(Result.initialized())
    val sendOtp: StateFlow<Result<String>> = _sendOtp.asStateFlow()

    private val _unsubscribeResult = MutableStateFlow<Result<String>>(Result.initialized())
    val unsubscribeResult: StateFlow<Result<String>> = _unsubscribeResult.asStateFlow()

    private val _deleteAccount = MutableStateFlow<Result<String>>(Result.initialized())
    val deleteAccountResult: StateFlow<Result<String>> = _deleteAccount.asStateFlow()

    fun fetchProfile() {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                var info: UserInfo? = null
                val data = asyncResultWithUserInfo(userPreferencesRepository) {
                    info = it
                    RetrofitHelper.apiService.getUser(it.id)
                }

                val view = data.map {
                    val imageUrl = it.profile?.imagePath.absoluteImageUrlPath()
                    val name = "${it.profile?.firstName} ${it.profile?.lastName}"
                    val mobile = it.profile?.mobile.toString()
                    val email = info?.email.toString()

                    ProfileView(imageUrl, name, email, mobile)
                }

                data.dataOrNull()?.let {
                    info?.let { info ->
                        info.basicProfile = it.profile
                        userPreferencesRepository.updateUserInformation(info)
                    }
                }

                _result.update { view }
            }  finally {
                _isLoading.update { false }
            }
        }
    }

    fun sendOtp() {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val view = asyncApiResultWithUserInfo(userPreferencesRepository) {
                    val requestDto = ResendOtpRequestDto(it.id, it.phoneNumber.toString())
                    RetrofitHelper.apiService.resendOtp(requestDto)
                }.toMessageResult()

                _sendOtp.update { view }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun uploadPicture(file: File) {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val view = asyncResultWithUserInfo(userPreferencesRepository) {
                    val requestFile = file.asRequestBody("image/jpg".toMediaType())
                    val image = MultipartBody.Part.createFormData("image", file.name, requestFile)
                    RetrofitHelper.apiService.uploadProfilePicture(it.id.toRequestBody("text/plain".toMediaType()), image)
                }.map {
                    it.absoluteImageUrlPath()
                }

                _uploadPictureResult.update {view }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun unsubscribeUser() {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val view = asyncApiResultWithUserInfo(userPreferencesRepository) {
                    RetrofitHelper.apiService.unsubscribeUser(it.id)
                }.toMessageResult()

                _unsubscribeResult.update { view }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun deleteAccount() {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val view = asyncApiResultWithUserInfo(userPreferencesRepository) {
                    RetrofitHelper.apiService.deleteAccount(it.id)
                }.toMessageResult()

                _deleteAccount.update {view }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    suspend fun logout() {
        userPreferencesRepository.deleteUserInformation()
    }

    fun reInitializeResult() {
        _result.update { Result.initialized() }
    }

    fun reInitializeOtpResult() {
        _sendOtp.update { Result.initialized() }
    }

    fun reInitializeUploadPictureResult() {
        _uploadPictureResult.update { Result.initialized() }
    }

    fun reInitializeUnsubscribeResult() {
        _unsubscribeResult.update { Result.initialized() }
    }

    fun reInitializeDeleteAccountResult() {
        _deleteAccount.update { Result.initialized() }
    }
}