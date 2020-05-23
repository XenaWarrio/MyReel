package dx.queen.myreel.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuViewModel : ViewModel() {
    val chat = MutableLiveData<String>()
    val search = MutableLiveData<String>()
    val personalInformation =  MutableLiveData<String>()
    val exit = MutableLiveData<String>()


    fun openChat(){
        chat.value = ""
    }

    fun openSearch(){
        search.value = ""
    }

    fun openPersonalInformation(){
        personalInformation.value = ""
    }

    fun exit(){
        exit.value = ""
    }
}