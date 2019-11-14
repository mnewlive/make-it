package com.mandarine.targetList.common.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class ConnectivityReceiver : BroadcastReceiver() {

    var networkStateListener: NetworkStateChangeListener? = null

    override fun onReceive(context: Context, intent: Intent?) {
        networkStateListener?.onNetworkConnectionChanged(isConnectedOrConnecting(context))
    }

    private fun isConnectedOrConnecting(context: Context): Boolean {
        val networkInfo = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)
        return networkInfo?.activeNetworkInfo != null
    }
}
