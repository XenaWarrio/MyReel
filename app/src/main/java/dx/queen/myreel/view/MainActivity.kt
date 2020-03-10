package dx.queen.myreel.view

import android.content.Context
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dx.queen.myreel.broadcast.ConnectivityReceiver
import dx.queen.myreel.R

class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private val registrationFragment = R.id.registrationFragment
    private val authorizationFragment = R.id.authorizationFragment

    private lateinit var wManager: WindowManager
    private lateinit var viewL: View
    private lateinit var navController: NavController

    private var isWindowVisible = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        viewL = layoutInflater.inflate(R.layout.window_no_connection, null)

        registerReceiver(
            ConnectivityReceiver(), IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )


        navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )

        navigate(registrationFragment)
    }


    fun navigateToRegistration() {
        navController.navigate(R.id.action_registrationFragment_to_loginFragment)
    }

    private fun navigate(fragment: Int) {
        navController.navigate(fragment)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!isConnected) {
            showWindow()
            isWindowVisible = true
        } else {
            if (isWindowVisible) {
                removeView()
            }
        }

    }


    private fun showWindow() {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE and WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP

        wManager.addView(viewL, params)
    }

    private fun removeView() {
        wManager.removeView(viewL)

    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

}
