package dx.queen.myreel.viewModel.rvChat

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dx.queen.myreel.databinding.MessageInChatBinding
import dx.queen.myreel.models.Message
import dx.queen.myreel.view.rememberUser.SharedPreferencesIsUserRegister


class ChatAdapter : RecyclerView.Adapter<MessageViewHolder>() {

    var  currentUserEmail = SharedPreferencesIsUserRegister.getUserEmail()

    private var listOfMessages = arrayListOf<Message>()

    fun addMessage(message: Message) {
        listOfMessages.add(message)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: MessageInChatBinding =
            MessageInChatBinding.inflate(layoutInflater, parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(listOfMessages[position].text)
    }

    override fun getItemCount(): Int {
        return listOfMessages.size
    }
}

class MessageViewHolder(private val binding: MessageInChatBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(text: String) {
        binding.messageBetweenUsers.text = text
    }
}


