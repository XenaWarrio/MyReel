package dx.queen.myreel.viewModel

import androidx.lifecycle.*
import dx.queen.myreel.R
import dx.queen.myreel.repository.Repository
import dx.queen.myreel.data_source.local.WorkWithDatabase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val repository = Repository()

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    var emailError = MutableLiveData<Int>()
    var passwordError = MutableLiveData<Int>()

    var emailNotConfirmed = MutableLiveData<Unit>()
    var fireBaseError = MutableLiveData<String>()
    val authSuccess = MutableLiveData<Unit>()

    var haveNoAccount = MutableLiveData<Unit>()

    private val emailNotConfirmedObserver = Observer<Unit> {
        emailNotConfirmed.value = Unit
    }
    private val fireBaseErrorObserver = Observer<String> {
        fireBaseError.value = it
    }

    private val authSucceedObserver = Observer<Unit> {
        authSuccess.value = Unit
       viewModelScope.launch {
            val job = async {
                repository.getUserInformationFromFireBase()
            }
            job.await()?.let{user->
                launch {
                    repository.saveUserToDB(user)
                }
            }
        }
    }


    fun signIn() {
        if (!checkIsCorrect()) return
        repository.signIn(email.value!!, password.value!!)

        repository.emailNotConfirmedForLogin.observeForever(emailNotConfirmedObserver)
        repository.authFireBaseFailedForLogin.observeForever(fireBaseErrorObserver)
        repository.authSucceedForLogin.observeForever(authSucceedObserver)
    }

    fun haveNoAccount(){
        haveNoAccount.value = Unit
    }

    private fun checkIsCorrect(): Boolean {
        val resultEmail =
            ValidationData.validateEmail(email.value)
        val resultPassword =
            ValidationData.validatePassword(password.value)
        var isDataCorrect = false
        when {
            resultEmail != R.string.ok -> emailError.value = resultEmail
            resultPassword != R.string.ok -> passwordError.value = resultPassword
            else -> isDataCorrect = true
        }
        return isDataCorrect
    }

    override fun onCleared() {
        super.onCleared()
        repository.emailNotConfirmedForLogin.removeObserver(emailNotConfirmedObserver)
        repository.authFireBaseFailedForLogin.removeObserver(fireBaseErrorObserver)
        repository.authSucceedForLogin.removeObserver(authSucceedObserver)
    }
}