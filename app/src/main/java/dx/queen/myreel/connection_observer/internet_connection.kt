package dx.queen.myreel.connection_observer

import android.net.ConnectivityManager
import android.net.Network

import java.util.Observable

/**
 * Class that implements [ConnectivityManager.NetworkCallback] in order to listen for changes on
 * network connection
 * */
class NetworkChangeReceiver : ConnectivityManager.NetworkCallback() {
    private var isNetworkConnected = false

    override fun onLost(network: Network?) {
        super.onLost(network)
        // Set the connectivity to be false
        isNetworkConnected = false
        observable.connectionChanged(false)
    }

    override fun onAvailable(network: Network?) {
        super.onAvailable(network)
        // Set the connectivity to be true
        isNetworkConnected = true
        observable.connectionChanged(true)
    }

    /**
     * Class that implements [Observable].
     * Has private constructor in order to prevent instantiation
     * */
    class NetworkObservable private constructor() : Observable() {

        /**
         * Function that notifies the observers about a network connection change
         *
         * @param isConnected           If the device has internet connection or not
         * */
        fun connectionChanged(isConnected: Boolean) {
            setChanged()

            // Pass the parameter and notify all its observers
            notifyObservers(isConnected)
        }

        // Create a Singleton pattern
        companion object {
            private var instance: NetworkObservable? = null

            /**
             * Function that returns a [NetworkObservable] instance
             * */
            fun getInstance(): NetworkObservable {
                // Create a Network observable instance if hasn't been set before, otherwise return the instance
                if (instance == null) {
                    instance = NetworkObservable()
                }

                return instance!! // instance won't be null by now
            }
        }
    }

    companion object {
        // Create a public static NetworkObservable instance
        val observable: NetworkObservable
            get() = NetworkObservable.getInstance()
    }
}
