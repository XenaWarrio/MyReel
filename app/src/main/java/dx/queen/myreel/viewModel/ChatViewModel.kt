package dx.queen.myreel.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dx.queen.myreel.models.Message
import dx.queen.myreel.repository.Repository

class ChatViewModel(private val companionId: String) : ViewModel() {
    private val repository = Repository()

    var message = MutableLiveData<String>()
    var clearTextField = MutableLiveData<String>()

    private val messageBetweenUsersObserver = Observer<Message> {
        messageBetweenUsers.value = it
    }

    var messageBetweenUsers = MutableLiveData<Message>()

//    fun sendMessage2(messageString: String) {
//        messageBetweenUsers.value =
//            Message(
//                id = "messageID",
//                fromUserId = "andreyID",
//                toCompanionId = "kseniaID",
//                text = messageString
//            )
//    }

    fun sendMessage() {
        clearTextField.value = " "
        repository.sendMessage(message.value!!, companionId)
        repository.message.observeForever(messageBetweenUsersObserver)
    }
}