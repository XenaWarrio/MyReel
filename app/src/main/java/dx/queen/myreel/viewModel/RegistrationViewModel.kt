package dx.queen.myreel.viewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    var haveAnAccount = MutableLiveData<Unit>()
    var haveToConfirmEmail = MutableLiveData<Unit>()

    var emailError = MutableLiveData<Int>()
    var passwordError = MutableLiveData<Int>()
    var usernameError = MutableLiveData<Int>()
    var authError = MutableLiveData<String>()

    var dateFragment = MutableLiveData<Unit>()
    var clearAllFields = MutableLiveData<Unit>()

    fun onClick() {
        if (!checkIsCorrect()) {
            return
        }
        viewModelScope.launch {
            repository.createNewUserRepository(
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
            haveToConfirmEmail.value = Unit
        }
        clearAllFields.value = Unit

    }

    fun pickADate() {
        dateFragment.value = Unit
    }

    fun haveAnAccount() {
        haveAnAccount.value =Unit
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