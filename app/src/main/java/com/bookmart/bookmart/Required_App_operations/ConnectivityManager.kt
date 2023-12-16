package com.bookmart.bookmart.Required_App_operations

    import android.content.Context
    import android.net.ConnectivityManager
    import android.net.NetworkCapabilities
    import android.os.Build

class InternetConnectionChecker(private val context: Context) {

        fun isInternetAvailable(): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            } else {
                val networkInfo = connectivityManager.activeNetworkInfo
                return networkInfo?.isConnected == true
            }
        }
    }


