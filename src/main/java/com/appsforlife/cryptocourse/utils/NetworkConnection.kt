package com.appsforlife.cryptocourse.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.*
import android.os.Build
import androidx.lifecycle.LiveData

class NetworkConnection(context: Context) : LiveData<Boolean>() {

//    private var connectivityManager: ConnectivityManager =
//        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
//
//    override fun onActive() {
//        super.onActive()
//        updateConnection()
//        when{
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ->{
//                connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback())
//            }
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ->{
//                lollipopNetworkRequest()
//            }
//            else ->{
//                context.registerReceiver(
//                    networkReceiver,
//                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//                )
//            }
//        }
//    }
//
//    override fun onInactive() {
//        super.onInactive()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback())
//        }else{
//            context.unregisterReceiver(networkReceiver)
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private fun lollipopNetworkRequest() {
//        val requiresBuilder = NetworkRequest.Builder()
//            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
//            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
//        connectivityManager.registerNetworkCallback(
//            requiresBuilder.build(),
//            connectivityManagerCallback()
//        )
//    }
//
//    private fun connectivityManagerCallback(): ConnectivityManager.NetworkCallback {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            networkCallback = object : ConnectivityManager.NetworkCallback() {
//                override fun onLost(network: Network) {
//                    super.onLost(network)
//                    postValue(false)
//                }
//
//                override fun onAvailable(network: Network) {
//                    super.onAvailable(network)
//                    postValue(true)
//                }
//            }
//            return networkCallback
//        } else {
//            throw IllegalAccessError("Error")
//        }
//
//    }
//
//    private val networkReceiver = object : BroadcastReceiver(){
//        override fun onReceive(p0: Context?, p1: Intent?) {
//            updateConnection()
//        }
//
//    }
//
//    private fun updateConnection(){
//        val activeNetwork:NetworkInfo? = connectivityManager.activeNetworkInfo
//        postValue(activeNetwork?.isConnected == true)
//    }

    private var connectivityManager: ConnectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    private val networkRequestBuilder: NetworkRequest.Builder = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

    override fun onActive() {
        super.onActive()
        updateConnection()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager.registerDefaultNetworkCallback(
                getConnectivityMarshmallowManagerCallback()
            )
            else -> {
                marshmallowNetworkAvailableRequest()
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun marshmallowNetworkAvailableRequest() {
        connectivityManager.registerNetworkCallback(
            networkRequestBuilder.build(),
            getConnectivityMarshmallowManagerCallback()
        )
    }

    private fun getConnectivityMarshmallowManagerCallback(): ConnectivityManager.NetworkCallback {
        connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                networkCapabilities.let { capabilities ->
                    if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(
                            NetworkCapabilities.NET_CAPABILITY_VALIDATED
                        )
                    ) {
                        postValue(true)
                    }
                }
            }

            override fun onLost(network: Network) {
                postValue(false)
            }
        }
        return connectivityManagerCallback
    }

    private fun updateConnection() {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected == true)
    }

}