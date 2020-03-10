package dx.queen.myreel.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dx.queen.myreel.R
import dx.queen.myreel.viewModel.VerifyEmailViewModel
import kotlinx.android.synthetic.main.fragment_verify_email.*

class VerifyEmailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verify_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(VerifyEmailViewModel::class.java)
        val ac = activity as MainActivity


        bt_done.setOnClickListener {
            progressBar_invisible.visibility = ProgressBar.VISIBLE
            viewModel.isEmailVerifiedFun() }

        bt_back.setOnClickListener {

        }


        val emailVerification = Observer<Boolean> {
            Log.d("EMAIL_VERIFICATION", " View observer isVeryfied = $it")

            if (it) {
                Log.d("EMAIL_VERIFICATION", " View navigate")

                ac.navigateToAppMenu()
            }
        }

        viewModel.isEmailVerified.observe(this, emailVerification)

    }
}