package dx.queen.myreel.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuViewModel : ViewModel() {
    val chat = MutableLiveData<Unit>()
    val search = MutableLiveData<Unit>()
    val personalInformation =  MutableLiveData<Unit>()
    val exit = MutableLiveData<Unit>()


    fun openChat(){
        chat.value = Unit
    }

    fun openSearch(){
        search.value = Unit
    }

    fun openPersonalInformation(){
        personalInformation.value = Unit
    }

    fun exit(){
        exit.value = Unit
    }
}