package dx.queen.myreel.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dx.queen.myreel.R
import dx.queen.myreel.databinding.FragmentRegistrationBinding
import dx.queen.myreel.viewModel.RegistrationViewModel

class RegistrationFragment : Fragment() {

    private lateinit var registrationBinding : FragmentRegistrationBinding
    private var selectedPhotoUrl: String? = null
    private val activity = MainActivity()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registrationBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_registration, container, false
        )
        val view = registrationBinding.root
        registrationBinding.lifecycleOwner = this

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        registrationBinding.viewModel = viewModel

        val haveAnAccount = Observer<String> {
            activity.navigate(R.id.authorizationFragment)
        }

        val dateOfBirthObserver = Observer<String> { date ->
            registrationBinding.dateOfBirth.text = date
        }

        val fragmentDatePicker = Observer<String> {
            val dateFragment = DatePickerFragment()
            dateFragment.show(parentFragmentManager, "Date Picker")
        }

        val clearAllFields = Observer<String> {
            registrationBinding.userEmail.text.clear()
            registrationBinding.userPassword.text.clear()
            registrationBinding.userName.text.clear()
            registrationBinding.dateOfBirth.text = ""

        }

        val emailErrorObserver = Observer<String> { error ->
            registrationBinding.userEmail.error = error

        }
        val passwordErrorObserver = Observer<String> { error ->
            registrationBinding.userPassword.error = error

        }
        val userNameErrorObserver = Observer<String> { error ->
            registrationBinding.userName.error = error

        }


        viewModel.clearAllFields.observe(this, clearAllFields)

        viewModel.emailError.observe(this, emailErrorObserver)
        viewModel.passwordError.observe(this, passwordErrorObserver)
        viewModel.usernameError.observe(this, userNameErrorObserver)

        viewModel.dateOfBirth.observe(this, dateOfBirthObserver)

        viewModel.dateFragment.observe(this, fragmentDatePicker)
        viewModel.haveAnAccount.observe(this, haveAnAccount)

        registrationBinding.btProfilePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }

        viewModel.imageUrl.value = selectedPhotoUrl

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            val selectedPhotoUri = data.data
            Log.d("TEST", "$context")
            val bitmap =
                MediaStore.Images.Media.getBitmap(context!!.contentResolver, selectedPhotoUri)
            registrationBinding.selectedImageView.setImageBitmap(bitmap)
            registrationBinding.btProfilePhoto.alpha = 0f

            if (selectedPhotoUri != null) {
                selectedPhotoUrl = selectedPhotoUri.toString()
            }

        }
    }

}