package dx.queen.myreel.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dx.queen.myreel.repository.Repository

class VerifyEmailViewModel : ViewModel() {
    private val repository = Repository()

    var isEmailVerified = MutableLiveData<Boolean>()

    fun isEmailVerifiedFun() {
        repository.isEmailVerified()
        Log.d("EMAIL_VERIFICATION", " View Model fun isEmailVerified  repository.isEmailConfirmed.value = ${repository.isEmailConfirmed.value}")

        isEmailVerified.value = repository.isEmailConfirmed.value
    }
}