package com.celar.celvisitas.Tools

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.celar.celvisitas.Activities.DashboardActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Looper.prepare()

        Handler().post() {
            Toast.makeText(baseContext, remoteMessage.notification?.title,Toast.LENGTH_LONG).show()
        }

        Looper.loop()
    }

}