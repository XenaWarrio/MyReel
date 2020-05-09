package dx.queen.myreel.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dx.queen.myreel.models.Message
import dx.queen.myreel.repository.chats.ChatRepository
import dx.queen.myreel.viewModel.rvChat.ChatAdapter

class ChatViewModel (companionId: String): ViewModel() {
    private val repository = ChatRepository(companionId)

    var message = MutableLiveData<String>()
    var clearTextField = MutableLiveData<String>()

    fun sendMessage() {
        Log.d("BUTTON_DOESNT_WORK", "method")
        clearTextField.value = " "
        repository.sendMessage(message.value!!)
    }
}