package com.matrix_maeny.taskmanager.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.matrix_maeny.taskmanager.data.SharedData

class NetworkingRepository(private val connectivityManager: ConnectivityManager) :
    MutableLiveData<Boolean>() {

    var isNetworkAvailable:Boolean = false

    constructor(application: Application) : this(application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) {

//        val networkCallback = object : ConnectivityManager.NetworkCallback() {
//            override fun onAvailable(network: Network) {
//                super.onAvailable(network)
//                postValue(true)
//            }
//
//            override fun onLost(network: Network) {
//                super.onLost(network)
//                postValue(false)
//            }
//
//            override fun onUnavailable() {
//                super.onUnavailable()
//                postValue(false)
//            }
//
//        }

        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
//            override fun onAvailable(network: Network) {
//                super.onAvailable(network)
//                postValue(true)
////                SharedData.isNetworkAvailable = true
//                isNetworkAvailable = true
//            }

            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
//                SharedData.isNetworkAvailable = false
                isNetworkAvailable = false
            }

            override fun onUnavailable() {
                super.onUnavailable()
                postValue(false)
//                SharedData.isNetworkAvailable = false
                isNetworkAvailable = false
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)

                isNetworkAvailable = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                if(isNetworkAvailable){
                    postValue(isNetworkAvailable)
                }

            }
        })

    }
}