package com.dataxing.indiapolls.ui

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.dataxing.indiapolls.ui.util.userPreferencesRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_information")

class MainApplication : Application() {
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val userRepository = this.userPreferencesRepository()
        MainScope().launch {
            val language = ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0]?.language
            userRepository.updateLanguage(language ?: "en")
        }
    }
}