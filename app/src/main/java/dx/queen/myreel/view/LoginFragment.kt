package dx.queen.myreel.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dx.queen.myreel.R
import dx.queen.myreel.databinding.FragmentLoginBinding
import dx.queen.myreel.viewModel.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login, container, false
        )

        val view = loginBinding.root
        loginBinding.lifecycleOwner = this
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ac = activity as MainActivity
        val viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginBinding.loginViewModel = viewModel

        val emailError = Observer<Int> {
            loginBinding.emailSignIn.error = requireContext().resources.getText(it)
        }
        val passwordError = Observer<Int> {
            loginBinding.passwordSignIn.error = requireContext().resources.getText(it)
        }
        val fireBaseError = Observer<String> {
            makeText(context, it, Toast.LENGTH_LONG).show()
        }
        val emailNotConfirmed = Observer<Unit> {
            makeText(context, R.string.verifyEmailError, Toast.LENGTH_LONG).show()
        }

        val signInSuccess = Observer<Unit> {
            ac.navigateFromLoginToMenu()
        }

        val toRegistration = Observer<String>{
            ac.navigateFromLoginToRegistration()
        }

        viewModel.emailError.observe(viewLifecycleOwner, emailError)
        viewModel.passwordError.observe(viewLifecycleOwner, passwordError)
        viewModel.fireBaseError.observe(viewLifecycleOwner, fireBaseError)
        viewModel.emailNotConfirmed.observe(viewLifecycleOwner, emailNotConfirmed)
        viewModel.authSuccess.observe(viewLifecycleOwner, signInSuccess)
        viewModel.haveNoAccount.observe(viewLifecycleOwner, toRegistration)
    }
}