package dx.queen.myreel.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dx.queen.myreel.R
import dx.queen.myreel.repository.Repository
import dx.queen.myreel.repository.WorkWithDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val repository = Repository()
    private val workWithDatabase = WorkWithDatabase()

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    var emailError = MutableLiveData<Int>()
    var passwordError = MutableLiveData<Int>()

    var emailNotConfirmed = MutableLiveData<String>()
    var fireBaseError = MutableLiveData<String>()
    var authSuccess = MutableLiveData<String>()

    var haveNoAccount = MutableLiveData<String>()

    private val emailNotConfirmedObserver = Observer<String> {
        emailNotConfirmed.value = it
    }
    private val fireBaseErrorObserver = Observer<String> {
        fireBaseError.value = it
    }

    private val authSucceedObserver = Observer<String> {
        authSuccess.value = ""
        GlobalScope.launch {
            val job = async {
                repository.getUserInformationFromFireBase()
            }
            job.await()?.let{user->
                launch {
                    workWithDatabase.saveUserToDB(user)
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
        haveNoAccount.value = "yes"
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