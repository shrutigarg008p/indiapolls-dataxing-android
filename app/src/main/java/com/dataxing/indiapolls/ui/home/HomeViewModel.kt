package com.dataxing.indiapolls.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.data.auth.login.UserInfo
import com.dataxing.indiapolls.network.RetrofitHelper
import com.dataxing.indiapolls.network.asyncResultWithUserInfo
import com.dataxing.indiapolls.ui.notification.TokenService
import com.dataxing.indiapolls.ui.util.absoluteImageUrlPath
import kotlinx.coroutines.launch

class HomeViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val tokenService = TokenService(userPreferencesRepository, viewModelScope)

    private val _result = MutableLiveData<Result<HomeHeaderView>>()
    val result: LiveData<Result<HomeHeaderView>> = _result

    fun fetchProfile() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                var info: UserInfo? = null
                val data = asyncResultWithUserInfo(userPreferencesRepository) {
                    info = it
                    RetrofitHelper.apiService.getUser(it.id)
                }

                data.dataOrNull()?.let {
                    info?.let { info ->
                        info.basicProfile = it.profile
                        userPreferencesRepository.updateUserInformation(info)
                    }
                }

                fetchUserInformation()
            }  finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun fetchUserInformation() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val userInfo = userPreferencesRepository.fetchUserInformation()
                userInfo?.let { info ->
                    userPreferencesRepository.updateUserInformation(info)
                    
                    val imageUrl = info.basicProfile?.imagePath.absoluteImageUrlPath()
                    val name = "${info.basicProfile?.firstName} ${info.basicProfile?.lastName}"
                    val email = info.email
                    _result.postValue(Result.Success(HomeHeaderView(imageUrl, name, email.toString())))
                }
            } catch (e: Exception) {
                _result.postValue(Result.Error(e))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}