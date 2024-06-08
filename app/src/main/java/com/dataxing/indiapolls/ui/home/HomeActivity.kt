package com.dataxing.indiapolls.ui.home

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.databinding.ActivityHomeBinding
import com.dataxing.indiapolls.ui.ViewModelFactory
import com.dataxing.indiapolls.ui.util.launchUrl
import com.dataxing.indiapolls.ui.util.showMessage
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging


class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    private val homeViewModel: HomeViewModel by viewModels { ViewModelFactory() }

    private val settingDialog: AlertDialog by lazy {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle(R.string.permission_dialog_title)
            .setMessage(R.string.notification_permission_dialog_message)
            .setPositiveButton(R.string.Okay) { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

        builder.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.appBarHome.appBarLayout.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_survey,
                R.id.nav_dashboard,
                R.id.nav_profiles,
                R.id.nav_rewards,
                R.id.nav_contact_us
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

                homeViewModel.fetchUserInformation()
                homeViewModel.fetchProfile()
            }
        })

        navView.getHeaderView(0).setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            navController.navigate(R.id.profileFragment)
        }

        navView.menu.findItem(R.id.nav_faq).setOnMenuItemClickListener {
            this.launchUrl(getString(R.string.faq_link))

            return@setOnMenuItemClickListener true
        }

        window.statusBarColor = Color.WHITE

        askNotificationPermission()

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            homeViewModel.tokenService.updateToken(it)
        }

        homeViewModel.result.observe(this) { result ->
            when(result) {
                is Result.Success -> {
                    (navView.getHeaderView(0) as? ViewGroup)?.let {
                        val image = it.findViewById<ImageView>(R.id.profileImage)
                        val name = it.findViewById<TextView>(R.id.name)
                        val email = it.findViewById<TextView>(R.id.email)
                        Glide
                            .with(this)
                            .load(result.data.imageUrl)
                            .centerCrop()
                            .placeholder(R.drawable.ic_avatar_placeholder)
                            .into(image)
                        name.text = result.data.name
                        email.text = result.data.email
                    }
                }
                is Result.Error -> {
                    this.showMessage(result.exception)
                }
                is Result.initialized -> {}
            }
        }

        homeViewModel.fetchProfile()
    }

    fun setNavViewCheckedItem(resId: Int) {
        binding.navView.menu.performIdentifierAction(resId, 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
            settingDialog.show()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}