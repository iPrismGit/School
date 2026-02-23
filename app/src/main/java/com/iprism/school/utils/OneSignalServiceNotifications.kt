package com.iprism.school.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.iprism.school.R
import com.iprism.school.activities.HomeActivity

import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class OneSignalServiceNotifications : OneSignal.OSRemoteNotificationReceivedHandler {

    private val CHANNEL_ID = "CALL_NOTIFICATION_CHANNEL"
    private var notifManager: NotificationManager? = null
    private var mChannel: NotificationChannel? = null
    var m = 0
    var intent: Intent? = null

    @SuppressLint("SuspiciousIndentation")
    override fun remoteNotificationReceived(
        context: Context,
        osNotificationReceivedEvent: OSNotificationReceivedEvent
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val notification = osNotificationReceivedEvent.notification
            Log.d("notification", "Data" + notification.additionalData)
            Log.d("notification", "notidications$notification")
            OneSignal.setNotificationOpenedHandler { result ->
                val data = result.notification.additionalData
                val customData = data?.optString("key_name", "")

                val intent = Intent(context, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("from", "notification")
                }
                context.startActivity(intent)
            }

            val data = notification.additionalData
            val status = data?.optString("status", "normal") ?: "normal"
            val token = data?.optString("token", "")
            val doctorName = data?.optString("doctor_name", "Unknown User")
            val doctorId = data?.optString("doctor_id", "")
            Log.d("doctorName", doctorName.toString())

            playNotificationSound(status, context)
            osNotificationReceivedEvent.complete(notification)
        }

    }

    private fun playNotificationSound(type: String, context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        try {
            if (type.equals("call", true)) {
//                val playIntent = Intent(this, SoundService::class.java)
//                playIntent.action = SoundService.ACTION_PLAY
//                context.startService(playIntent)
            } else if (type.equals("booking", true)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            500,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(500) // Vibrate for 500 milliseconds
                }
                val rawPathUri =
                    Uri.parse("android.resource://" + context.packageName + "/" + R.raw.order_alert)
                val r = RingtoneManager.getRingtone(context.applicationContext, rawPathUri)
                r.play()
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            500,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(500) // Vibrate for 500 milliseconds
                }
                val rawPathUri =
                    Uri.parse("android.resource://" + context.packageName + "/" + R.raw.message_tone)
                val r = RingtoneManager.getRingtone(context.applicationContext, rawPathUri)
                r.play()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    private fun startSound(context: Context) {
//        val playIntent = Intent(context, SoundService::class.java)
//        playIntent.action = SoundService.ACTION_PLAY
//        context.startService(playIntent)
//        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            ContextCompat.startForegroundService(context, intent!!)
//        } else {
//            context.startService(intent)
//        }*/
//        /*Handler().postDelayed({
//            //empSound("update")
//        }, 30000)*/
//    }

    private fun startService(
        context: Context,
        serviceClass: Class<*>,
        action: String,
        token: String?,
        doctorName: String?,
        doctorId: String?
    ) {
        val intent = Intent(context, serviceClass).apply {
            putExtra("action", action)
            putExtra("token", token)
            putExtra("doctorName", doctorName)
            putExtra("doctorId", doctorId)
        }
        context.startService(intent)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, intent)
        } else {
            context.startService(intent)
        }*/
    }

    private fun wakeUpScreen(context: Context) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "MyApp::NotificationWakeLock"
        )
        wakeLock.acquire(3000)
        wakeLock.release()
    }
}