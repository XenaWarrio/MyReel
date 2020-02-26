package dx.queen.myreel.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dx.queen.myreel.R

class MainActivity : AppCompatActivity() {

    val registrationFragment = R.id.registrationFragment
    val authorizationFragment = R.id.authorizationFragment

    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )

        navigate(registrationFragment)
    }

    fun navigate(fragment: Int) {
        navController.navigate(fragment)
    }


}
