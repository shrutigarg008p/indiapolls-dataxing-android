package com.dataxing.indiapolls.ui.onboarding

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.databinding.ActivityOnBoardingBinding
import com.dataxing.indiapolls.ui.ViewModelFactory

class OnBoardingActivity : AppCompatActivity() {

    private val viewModel: OnBoardingViewModel by viewModels { ViewModelFactory() }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        viewModel.initialize()

        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.appBarLayout.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navController = findNavController(R.id.nav_host_fragment_content_on_boarding)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_on_boarding)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}