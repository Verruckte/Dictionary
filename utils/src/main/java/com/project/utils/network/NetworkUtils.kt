package com.project.utils.network

import android.content.Context
import android.net.*
import androidx.lifecycle.LiveData

class OnlineLiveData(context: Context) : LiveData<Boolean>() {

    private val availableNetworks = mutableSetOf<Network>()
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val request: NetworkRequest = NetworkRequest.Builder().build()
    private val callback = object : ConnectivityManager.NetworkCallback() {

        override fun onLost(network: Network) {
            println("NETWORK onLost")
            availableNetworks.remove(network)
            update(availableNetworks.isNotEmpty())
        }

        override fun onAvailable(network: Network) {
            println("NETWORK onAvailable")
            availableNetworks.add(network)
            update(availableNetworks.isNotEmpty())
        }

        override fun onUnavailable() {
            println("NETWORK onUnavailable")
            availableNetworks.clear()
            update(availableNetworks.isNotEmpty())
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
        }
    }

    override fun onActive() {
        connectivityManager.registerNetworkCallback(request, callback)
    }

    override fun onInactive() {
        connectivityManager.unregisterNetworkCallback(callback)
    }

    private fun update(online: Boolean) {
        if (online != value) {
            postValue(online)
        }
    }
}