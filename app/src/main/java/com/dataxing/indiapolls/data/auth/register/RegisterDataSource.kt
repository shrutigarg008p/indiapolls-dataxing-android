package com.dataxing.indiapolls.data.auth.register

import com.dataxing.indiapolls.data.Result
import java.io.IOException
import java.util.UUID

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class RegisterDataSource {

    fun register(username: String, password: String): Result<RegisteredUser> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = RegisteredUser(UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}