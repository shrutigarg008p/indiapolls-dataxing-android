package com.dataxing.indiapolls.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.data.auth.DeviceTokenRequestDto
import com.dataxing.indiapolls.network.RetrofitHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TokenService(private val userPreferencesRepository: UserPreferencesRepository, private val coroutineScope: CoroutineScope) {
    private val _result = MutableLiveData<Result<Any?>>()
    val result: LiveData<Result<Any?>> = _result

    fun updateToken(token: String) {
        coroutineScope.launch {
            try {
                val userInfo = userPreferencesRepository.fetchUserInformation()
                userInfo?.let { info ->
                    val requestDto = DeviceTokenRequestDto(info.id, token)
                    val result = RetrofitHelper.apiService.updateDeviceToken(requestDto)
                    val apiResult = result.body()
                    if (result.isSuccessful && apiResult != null && apiResult.status == 1) {
                        _result.postValue(Result.Success(apiResult.message))
                    } else {
                        _result.postValue(Result.Error(Exception(apiResult?.message)))
                    }
                }
            } catch (e: Exception) {
                _result.postValue(Result.Error(e))
            }
        }
    }
}