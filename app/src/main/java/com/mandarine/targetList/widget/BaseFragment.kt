package com.mandarine.targetList.widget

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import androidx.fragment.app.Fragment
import com.mandarine.targetList.common.network.ConnectivityReceiver
import com.mandarine.targetList.common.network.NetworkStateChangeListener

abstract class BaseFragment : Fragment(), NetworkStateChangeListener {

    private val connectivityReceiver = ConnectivityReceiver()

    override fun onResume() {
        super.onResume()
        activity?.registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        connectivityReceiver.networkStateListener = this
    }

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(connectivityReceiver)
        connectivityReceiver.networkStateListener = null
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) Log.d("some", "isConnected")
        else Log.d("some", "is not connected")
    }
}
