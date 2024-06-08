package com.dataxing.indiapolls.data


sealed class Result<out T : Any?> {

    data class Success<out T : Any?>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data class initialized(val isInitialized: Boolean = true) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is initialized -> "Initialized[isInitialized=$isInitialized]"
        }
    }

    fun  dataOrNull(): T? {
       return (this as? Success<T>)?.data
    }

    fun  requireData(): T {
        return (this as Success<T>).data
    }
}