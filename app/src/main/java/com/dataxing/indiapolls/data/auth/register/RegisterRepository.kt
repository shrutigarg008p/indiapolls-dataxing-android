package com.dataxing.indiapolls.data.auth.register

import com.dataxing.indiapolls.data.Result

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class RegisterRepository(val dataSource: RegisterDataSource) {

    // in-memory cache of the loggedInUser object
    var user: RegisteredUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun register(username: String, password: String): Result<RegisteredUser> {
        // handle login
        val result = dataSource.register(username, password)

        if (result is Result.Success) {
            setRegisteredUser(result.data)
        }

        return result
    }

    private fun setRegisteredUser(registeredUser: RegisteredUser) {
        this.user = registeredUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}