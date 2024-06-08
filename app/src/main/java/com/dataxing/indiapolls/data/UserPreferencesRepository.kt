package com.dataxing.indiapolls.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dataxing.indiapolls.data.auth.login.UserInfo
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferencesRepository (private val dataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val LANGUAGE = stringPreferencesKey("language")
        val USER_INFO = stringPreferencesKey("user_info")
    }

    data class UserPreferences(
        val userInformation: UserInfo?,
        val language: String
    )

    val userPreferences: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        // Get the sort order from preferences and convert it to a [SortOrder] object
        val info = fetchUserInformation(preferences)
        val language = fetchLanguage(preferences)
        return UserPreferences(info, language)
    }

    suspend fun fetchUserInformation(): UserInfo? {
         val preferences = dataStore.data.first()
        val json = preferences[PreferencesKeys.USER_INFO]
        return json?.let { Gson().fromJson(it, UserInfo::class.java) }
    }

    private fun fetchUserInformation(preferences: Preferences): UserInfo? {
        val json = preferences[PreferencesKeys.USER_INFO]
        return json?.let { Gson().fromJson(it, UserInfo::class.java) }
    }

    suspend fun updateUserInformation(userInfo: UserInfo) {
        val info = Gson().toJson(userInfo)
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_INFO] = info
        }
    }

    private fun fetchLanguage(preferences: Preferences): String {
        val language = preferences[PreferencesKeys.LANGUAGE] ?: "en"
        currentLanguage = language
        return language
    }

    suspend fun updateLanguage(language: String) {
        dataStore.edit { preferences ->
            currentLanguage = language
            preferences[PreferencesKeys.LANGUAGE] = language
        }
    }

    suspend fun deleteUserInformation() {
        dataStore.edit {
            it.remove(PreferencesKeys.USER_INFO)
        }
    }

    companion object {
        var currentLanguage: String = "en"

        @Volatile
        private var instance: UserPreferencesRepository? = null

        fun getInstance(dataStore: DataStore<Preferences>) =
            instance ?: synchronized(this) {
                instance ?: UserPreferencesRepository(dataStore).also { instance = it }
            }
    }
}