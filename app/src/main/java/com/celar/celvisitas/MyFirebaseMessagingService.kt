package com.celar.celvisitas

import android.content.ContentValues
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.celar.celvisitas.Activities.DashboardActivity
import com.celar.celvisitas.Activities.LoginActivity
import com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val context: Context? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Looper.prepare()

        Handler().post() {
            Toast.makeText(baseContext, remoteMessage.notification?.title,Toast.LENGTH_LONG).show()
        }

        Looper.loop()

    }




}