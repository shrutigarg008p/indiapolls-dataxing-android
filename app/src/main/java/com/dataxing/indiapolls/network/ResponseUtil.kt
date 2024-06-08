package com.dataxing.indiapolls.network

import com.dataxing.indiapolls.data.ApiError
import com.dataxing.indiapolls.data.ApiResult
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.data.auth.login.UserInfo
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.Response
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

suspend fun <T> asyncResult(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Response<ApiResult<T>>
): Result<T> {
    return coroutineScope {
        async(context, start) {
            try {
                val result = block()
                val apiResult = result.body()
                if (result.isSuccessful && apiResult != null && apiResult.status == 1) {
                    Result.Success(apiResult.data)
                } else {
                   val message = result.errorBody()?.charStream()?.let {
                       Gson().fromJson(it, ApiError::class.java).message
                    }.toString()

                    Result.Error(Exception(message))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }.await()
    }
}

suspend fun <T> asyncResultWithUserInfo(
    userPreferencesRepository: UserPreferencesRepository,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.(UserInfo) -> Response<ApiResult<T>>
): Result<T> {
    return coroutineScope {
        async(context, start) {
            try {
                val userInfo = userPreferencesRepository.fetchUserInformation()
                if (userInfo != null) {
                    val result = block(userInfo)
                    val apiResult = result.body()
                    if (result.isSuccessful && apiResult != null && apiResult.status == 1) {
                        Result.Success(apiResult.data)
                    } else {
                        val message = result.errorBody()?.charStream()?.let {
                            Gson().fromJson(it, ApiError::class.java).message
                        }.toString()

                        Result.Error(Exception(message))
                    }
                } else {
                    Result.Error(Exception(""))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }.await()
    }
}

suspend fun <T> asyncApiResultWithUserInfo(
    userPreferencesRepository: UserPreferencesRepository,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.(UserInfo) -> Response<ApiResult<T>>
): Result<ApiResult<T>> {
    return coroutineScope {
        async(context, start) {
            try {
                val userInfo = userPreferencesRepository.fetchUserInformation()
                if (userInfo != null) {
                    val result = block(userInfo)
                    val apiResult = result.body()
                    if (result.isSuccessful && apiResult != null && apiResult.status == 1) {
                        Result.Success(apiResult)
                    } else {
                        val message = result.errorBody()?.charStream()?.let {
                            Gson().fromJson(it, ApiError::class.java).message
                        }.toString()

                        Result.Error(Exception(message))
                    }
                } else {
                    Result.Error(Exception(""))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }.await()
    }
}

suspend fun <T> asyncApiResultWithUserId(
    userId: String?,
    userPreferencesRepository: UserPreferencesRepository,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.(String) -> Response<ApiResult<T>>
): Result<ApiResult<T>> {
    return coroutineScope {
        async(context, start) {
            try {
                val id = userId ?: userPreferencesRepository.fetchUserInformation()?.id
                if (id != null) {
                    val result = block(id)
                    val apiResult = result.body()
                    if (result.isSuccessful && apiResult != null && apiResult.status == 1) {
                        Result.Success(apiResult)
                    } else {
                        val message = result.errorBody()?.charStream()?.let {
                            Gson().fromJson(it, ApiError::class.java).message
                        }.toString()

                        Result.Error(Exception(message))
                    }
                } else {
                    Result.Error(Exception(""))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }.await()
    }
}

suspend fun <T> asyncApiResult(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Response<ApiResult<T>>
): Result<ApiResult<T>> {
    return coroutineScope {
        async(context, start) {
            try {
                val result = block()
                val apiResult = result.body()
                if (result.isSuccessful && apiResult != null && apiResult.status == 1) {
                    Result.Success(apiResult)
                } else {
                    val message = result.errorBody()?.charStream()?.let {
                        Gson().fromJson(it, ApiError::class.java).message
                    }.toString()

                    Result.Error(Exception(message))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }.await()
    }
}

fun Response<ApiResult<*>>.toApiResult(): Result<ApiResult<*>> {
    val apiResult = this.body()
    return if (this.isSuccessful && apiResult != null && apiResult.status == 1) {
        Result.Success(apiResult)
    } else {
        val message = this.errorBody()?.charStream()?.let {
            Gson().fromJson(it, ApiError::class.java).message
        }.toString()

        Result.Error(Exception(message))
    }
}

fun Response<ApiResult<*>>.toResult(): Result<*> {
    val apiResult = this.body()
    return if (this.isSuccessful && apiResult != null && apiResult.status == 1) {
        Result.Success(apiResult.data)
    } else {
        val message = this.errorBody()?.charStream()?.let {
            Gson().fromJson(it, ApiError::class.java).message
        }.toString()

        Result.Error(Exception(message))
    }
}

fun Result<ApiResult<*>>.toMessageResult(): Result<String> {
    return when(this) {
        is Result.Success -> Result.Success(this.data.message)
        is Result.Error -> Result.Error(this.exception)
        is Result.initialized -> Result.initialized()
    }
}

fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when(this) {
        is Result.Success -> Result.Success(transform(this.data))
        is Result.Error -> Result.Error(this.exception)
        is Result.initialized -> Result.initialized()
    }
}