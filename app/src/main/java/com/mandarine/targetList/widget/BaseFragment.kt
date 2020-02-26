package com.mandarine.targetList.widget

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment
import com.mandarine.targetList.common.network.ConnectivityReceiver
import com.mandarine.targetList.common.network.NetworkStateChangeListener
import com.mandarine.targetList.interfaces.ActivityComponentsContract

abstract class BaseFragment : Fragment(), NetworkStateChangeListener {

    private val connectivityReceiver = ConnectivityReceiver()
    val activityComponents: ActivityComponentsContract?
        get() = activity as? ActivityComponentsContract

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

    override fun onNetworkConnectionChanged(isConnected: Boolean) {} //Use the method where you want to determine your Internet connection
}
