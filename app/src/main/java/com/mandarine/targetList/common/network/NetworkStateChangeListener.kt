package com.mandarine.targetList.common.network

interface NetworkStateChangeListener {
    fun onNetworkConnectionChanged(isConnected: Boolean)
}
