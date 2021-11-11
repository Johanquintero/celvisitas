package com.celar.celvisitas

import android.content.Context
import android.util.Log
import com.celar.celvisitas.Activities.LoginActivity
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseInstanceIDService: FirebaseMessagingService() {
    private val context: Context? = null


    override fun onNewToken(token: String) {
        Log.d("Firebase", "Refreshed token: $token")

        if (context is LoginActivity) {
            (context as LoginActivity).updateTokenIdFirebase(token)
        }else{
            var activityLogin: LoginActivity = LoginActivity()
            activityLogin.updateTokenIdFirebase(token)
        }
    }
}