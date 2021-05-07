package dx.queen.myreel.view

import android.content.Context
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import dx.queen.myreel.R
import dx.queen.myreel.broadcast.ConnectivityReceiver
import dx.queen.myreel.viewModel.rvChats.ChatsItem

class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private val registrationFragment = R.id.registrationFragment

    private lateinit var viewL: View

    private lateinit var navController: NavController

    private lateinit var wManager: WindowManager
    private var isWindowVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        viewL = layoutInflater.inflate(R.layout.window_no_connection, null)

//        registerReceiver(
//            ConnectivityReceiver(), IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
//        )
        navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )

        // We're getting the current user from Firebase
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Check if the current user is not null (if it is then send the current user into the RegistrationFragment)
//        if (currentUser != null) {
//            navController.navigate(R.id.menuFragment)
//        } else {
            navController.navigate(registrationFragment)
        //}
    }

    fun openCertainChat(chatItems: ChatsItem) {
        val bundle = bundleOf("chatsItem" to chatItems)

        navController.navigate(R.id.action_chatsFragment_to_chatFragment, bundle)
    }

    fun navigateFromRegistrationToLogin() {
        navController.navigate(R.id.action_registrationFragment_to_loginFragment)
    }

    fun navigateFromMenuToRegistration() {
        navController.navigate(R.id.action_menuFragment_to_registrationFragment)
    }

    fun navigateFromMenuToChats() {
        navController.navigate(R.id.action_menuFragment_to_chatsFragment)
    }

    fun navigateFromMenuToPersonalInformation() {
        navController.navigate(R.id.action_menuFragment_to_personalInformation)
    }

    fun navigateFromMenuToSearch() {
        navController.navigate(R.id.action_menuFragment_to_searchFragment)
    }

    fun navigateFromLoginToRegistration() {
        navController.navigate(
            R.id.action_loginFragment_to_registrationFragment, null,
            NavOptions.Builder()
                .setPopUpTo(
                    registrationFragment,
                    true
                ).build()
        )
    }

    fun navigateFromLoginToMenu() {
        navController.navigate(R.id.action_loginFragment_to_menuFragment)
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

    // show window with no internet connection
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(ConnectivityReceiver())
    }
}
