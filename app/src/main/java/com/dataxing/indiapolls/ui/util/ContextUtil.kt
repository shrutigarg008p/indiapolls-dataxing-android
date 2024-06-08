package com.dataxing.indiapolls.ui.util

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.UserPreferencesRepository
import com.dataxing.indiapolls.ui.MainApplication
import com.dataxing.indiapolls.ui.userDataStore


fun Application?.userPreferencesRepository(): UserPreferencesRepository {
    val myApplication: MainApplication = this as MainApplication
    return UserPreferencesRepository.getInstance(myApplication.userDataStore)
}

fun Context?.showMessage(resourceId: Int) {
    val message = this?.getString(resourceId) ?: return
    this.showMessage(message)
}

fun Context?.showMessage(message: String) {
    val appContext = this ?: return
    val builder: AlertDialog.Builder = AlertDialog.Builder(appContext)
    builder
        .setMessage(message)
        .setPositiveButton(R.string.Okay) { dialog, _ ->
            dialog.dismiss()
        }

    val dialog: AlertDialog = builder.create()
    dialog.show()
}

fun Context?.showMessage(exception: Exception) {
    this.showMessage(exception.message.toString())
}

fun Context.launchUrl(url: String) {
    var uri = Uri.parse(url)
    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        uri = Uri.parse("http://$url")
    }
    val intent = CustomTabsIntent.Builder()
        .build()
    intent.launchUrl(this, uri)
}