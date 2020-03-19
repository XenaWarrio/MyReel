package dx.queen.myreel.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dx.queen.myreel.repository.Repository

class VerifyEmailViewModel : ViewModel() {
    private val repository = Repository()

    var isEmailVerified = MutableLiveData<Boolean>()

//    fun isEmailVerifiedFun() {
//        repository.isEmailVerified()
//        isEmailVerified.value = repository.isEmailConfirmed.value
//    }
}