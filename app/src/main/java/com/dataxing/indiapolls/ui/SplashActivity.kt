package com.dataxing.indiapolls.ui

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.dataxing.indiapolls.ui.home.HomeActivity
import com.dataxing.indiapolls.ui.onboarding.OnBoardingActivity
import com.dataxing.indiapolls.ui.util.userPreferencesRepository
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { true }
        Handler(Looper.getMainLooper()).postDelayed({
            val application = application as MainApplication
            val userRepository = application.userPreferencesRepository()
            lifecycleScope.launch {
                val language = ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0]?.language
                userRepository.updateLanguage(language ?: "en")
                userRepository.userPreferences.collect {
                    val intent = if (it.userInformation == null) {
                        Intent(this@SplashActivity, MainActivity::class.java)
                    } else {
                        if (it.userInformation.basicProfile == null) {
                            Intent(this@SplashActivity, OnBoardingActivity::class.java)
                        } else {
                            Intent(this@SplashActivity, HomeActivity::class.java)
                        }
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }, 3000)
    }
}