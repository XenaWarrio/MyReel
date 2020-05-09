package dx.queen.myreel.viewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dx.queen.myreel.R
import dx.queen.myreel.repository.Repository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RegistrationViewModel : ViewModel() {
    private val repository = Repository()

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var username = MutableLiveData<String>()
    var dateOfBirth = MutableLiveData<String>()
    var imageUrl = MutableLiveData<String>()
    var haveAnAccount = MutableLiveData<String>()
    var haveToConfirmEmail = MutableLiveData<String>()

    var emailError = MutableLiveData<Int>()
    var passwordError = MutableLiveData<Int>()
    var usernameError = MutableLiveData<Int>()
    var authError = MutableLiveData<String>()

    var dateFragment = MutableLiveData<String>()
    var clearAllFields = MutableLiveData<String>()

    fun onClick() {
        if (!checkIsCorrect()) {
            return
        }
        GlobalScope.launch {
            repository.createNewUser(
                email.value!!,
                password.value!!,
                username.value!!,
                imageUrl.value,
                dateOfBirth.value!!
            )
        }
        repository.authFailedForRegistration.observeForever {
            authError.value = it
        }
        repository.authHaveToConfirmEmailForRegistration.observeForever {
            haveToConfirmEmail.value = it
        }
        clearAllFields.value = "clear"

    }

    fun pickADate() {
        dateFragment.value = "open"
    }

    fun haveAnAccount() {
        haveAnAccount.value = "yes"
    }


    private fun checkIsCorrect(): Boolean {
        val resultEmail = ValidationData.validateEmail(email.value)
        val resultPassword =
            ValidationData.validatePassword(password.value)
        val resultUserName =
            ValidationData.validateUserName(username.value)

        var isDataCorrect = false

        when {
            resultEmail != R.string.ok -> emailError.value = resultEmail
            resultPassword != R.string.ok -> passwordError.value = resultPassword
            resultUserName != R.string.ok -> usernameError.value = resultUserName
            else -> isDataCorrect = true
        }

        return isDataCorrect
    }

}