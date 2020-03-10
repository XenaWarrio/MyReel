package dx.queen.myreel.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import dx.queen.myreel.R
import dx.queen.myreel.databinding.FragmentRegistrationBinding
import dx.queen.myreel.viewModel.RegistrationViewModel
import java.text.DateFormat
import java.util.*


class RegistrationFragment : Fragment() {


    private lateinit var registrationBinding: FragmentRegistrationBinding


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
        val ac = activity as MainActivity


        val viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        registrationBinding.viewModel = viewModel


        val haveAnAccount = Observer<String> {
            ac.navigateToLogin()
        }

        val confirmEmail = Observer<String> {
            ac.navigateToVerifyEmail()
        }

        val dateOfBirthObserver = Observer<String> { date ->
            registrationBinding.dateOfBirth.text = date
        }

        val fragmentDatePicker = Observer<String> {
            openDatePicker(viewModel)
        }

        val clearAllFields = Observer<String> {
            registrationBinding.userEmail.text.clear()
            registrationBinding.userPassword.text.clear()
            registrationBinding.userName.text.clear()
            registrationBinding.dateOfBirth.text = ""

        }

        viewModel.clearAllFields.observe(this, clearAllFields)
        viewModel.dateOfBirth.observe(this, dateOfBirthObserver)
        viewModel.dateFragment.observe(this, fragmentDatePicker)
        viewModel.haveAnAccount.observe(this, haveAnAccount)
        viewModel.authConfirmEmail.observe(this, confirmEmail)

        if (context != null) {

            val emailErrorObserver = Observer<Int> { error ->
                registrationBinding.userEmail.error = context!!.resources.getText(error)

            }
            val passwordErrorObserver = Observer<Int> { error ->
                registrationBinding.userPassword.error = context!!.resources.getText(error)

            }
            val userNameErrorObserver = Observer<Int> { error ->
                registrationBinding.userName.error = context!!.resources.getText(error)

            }

            val userAuthErrorObserver = Observer<String> { error ->
                makeText(context, error, Toast.LENGTH_LONG).show()
            }


            viewModel.emailError.observe(this, emailErrorObserver)
            viewModel.passwordError.observe(this, passwordErrorObserver)
            viewModel.usernameError.observe(this, userNameErrorObserver)
            viewModel.authError.observe(this, userAuthErrorObserver)
        }


        registrationBinding.btProfilePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)


        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            val selectedPhotoUri = data.data
            val bitmap =
                MediaStore.Images.Media.getBitmap(context!!.contentResolver, selectedPhotoUri)
            registrationBinding.selectedImageView.setImageBitmap(bitmap)
            registrationBinding.btProfilePhoto.alpha = 0f

            if (selectedPhotoUri != null) {
                registrationBinding.viewModel!!.imageUrl.value = selectedPhotoUri.toString()

            }

        }
    }

    private fun openDatePicker(viewModel: RegistrationViewModel) {

        val calendar = Calendar.getInstance()
        val builder: MaterialDatePicker.Builder<Long> =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(dx.queen.myreel.R.string.dateOfBirth)
                .setSelection(calendar.timeInMillis)

        builder.setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
        val picker: MaterialDatePicker<Long> = builder.build()

        picker.addOnPositiveButtonClickListener { data ->
            val dateOfBirth = DateFormat.getDateInstance().format(data)
            viewModel.dateOfBirth.value = dateOfBirth
        }

        picker.show(parentFragmentManager, picker.toString())
    }

}