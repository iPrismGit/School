package com.iprism.school.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.emoji2.text.EmojiCompat
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal
import com.onesignal.OneSignal.OSNotificationWillShowInForegroundHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MyApp : Application() {

    private lateinit var networkConnection: NetworkConnectionLIveData
    private var currentActivity: Activity? = null
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)


    override fun onCreate() {
        super.onCreate()
        EmojiCompat.init(this)
        OneSignal.initWithContext(this)
        OneSignal.setAppId("e9cb5d03-036e-4f79-9a9c-d40882633bdb")
        Log.d("OneSignal", "Device is subscribed: " + OneSignal.getDeviceState()!!.isSubscribed())

        OneSignal.setNotificationWillShowInForegroundHandler(
            OSNotificationWillShowInForegroundHandler { notificationReceivedEvent: OSNotificationReceivedEvent? ->
                Log.d(
                    "OneSignalNotification",
                    "Title: " + notificationReceivedEvent!!.getNotification().getTitle()
                )
                Log.d(
                    "OneSignalNotification",
                    "Body: " + notificationReceivedEvent.getNotification().getBody()
                )
                notificationReceivedEvent.complete(notificationReceivedEvent.getNotification())
            })

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity

                appScope.launch {
                    val isNetwork = NetworkUtil.isNetworkAvailable(activity)
                    val hasInternet = if (isNetwork) NetworkUtil.hasInternetAccess() else false

                    if (!isNetwork || !hasInternet) {
                        NoInternetDialog.show(activity)
                    } else {
                        NoInternetDialog.dismiss()
                    }
                }
            }

            override fun onActivityPaused(activity: Activity) {
                if (currentActivity == activity) currentActivity = null
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })

        networkConnection = NetworkConnectionLIveData(this)
        networkConnection.observeForever { isConnected ->
            val activity = currentActivity ?: return@observeForever

            if (isConnected) {
                appScope.launch {
                    val hasInternet = NetworkUtil.hasInternetAccess()
                    if (hasInternet) {
                        NoInternetDialog.dismiss()
                    } else {
                        NoInternetDialog.show(activity)
                    }
                }
            } else {
                NoInternetDialog.show(activity)
            }
        }
    }

}
