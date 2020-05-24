package dx.queen.myreel.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dx.queen.myreel.models.Message
import dx.queen.myreel.repository.Repository
import dx.queen.myreel.utils.clearPoints

class ChatViewModel(private val companionEmail: String) : ViewModel() {
    private val repository = Repository()

    var message = MutableLiveData<String>()
    var clearTextField = MutableLiveData<String>()

    private val messageBetweenUsersObserver = Observer<Message> {

        messageBetweenUsers.value = it
    }

    var messageBetweenUsers = MutableLiveData<Message>()


    fun sendMessage() {
        repository.sendMessage(message.value!!, companionEmail.clearPoints())
        repository.messageLiveData.observeForever(messageBetweenUsersObserver)
        clearTextField.value = " "

    }
}