package com.mandarine.target_list

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService: FirebaseMessagingService() {

    private val TAG = "some"

    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")
    }
}