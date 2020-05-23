package dx.queen.myreel.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


class ConnectivityReceiver : BroadcastReceiver() {
    private var isInternetOn = false

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
    }


    override fun onReceive(p0: Context?, p1: Intent?) {
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(isConnectedOrConnecting(p0!!))
        }
    }


     fun isConnectedOrConnecting(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT < 23) {
            val networkInfo = connectivityManager.activeNetworkInfo

            if (networkInfo != null) {
                return (networkInfo.isConnected && (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE))
            }
        } else {
            val activeNetwork = connectivityManager.activeNetwork

            if (activeNetwork != null) {
                val networkCapabilities =
                    connectivityManager.getNetworkCapabilities(activeNetwork)

                return (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                ))
            }
        }
        return false
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    fun IsInternetOn()= isInternetOn


}
