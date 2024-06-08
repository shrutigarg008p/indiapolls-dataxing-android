package com.dataxing.indiapolls.ui.util

import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class Permissions<I>(private val activity: Activity, private val registry : ActivityResultRegistry) :
    DefaultLifecycleObserver {

    lateinit var getContent : ActivityResultLauncher<String>

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        getContent = registry.register("key", owner, ActivityResultContracts.GetContent()) { uri ->
            // Handle the returned Uri
        }
    }
}