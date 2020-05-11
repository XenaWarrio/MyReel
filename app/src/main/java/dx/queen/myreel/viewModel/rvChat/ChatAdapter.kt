package dx.queen.myreel.viewModel.rvChat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dx.queen.myreel.databinding.MessageInChatBinding
import dx.queen.myreel.models.Message
import dx.queen.myreel.view.rememberUser.SharedPreferencesIsUserRegister
import kotlinx.android.synthetic.main.message_user.view.*


class ChatAdapter : RecyclerView.Adapter<MessageViewHolder>() {

     var currentUserId = SharedPreferencesIsUserRegister.getUserId()


   private var listOfMessages = arrayListOf<Message>()

    fun addMessage(message:Message){
        listOfMessages.add(message)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        Log.d("BUTTON_DOESNT_WORK", "on create view holder +  adapter userId = $currentUserId")

        val layoutInflater = LayoutInflater.from(parent.context)
        val view: MessageInChatBinding = MessageInChatBinding.inflate(layoutInflater, parent, false)
        return MessageViewHolder(view.root)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        Log.d("BUTTON_DOESNT_WORK", "ON BIND View holder message = ${listOfMessages[position].text}  adapter userId = $currentUserId")

        holder.bind(listOfMessages[position].text)
    }

    override fun getItemCount(): Int {
        return listOfMessages.size
    }
}

class MessageViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    fun bind(text: String) {
        Log.d("BUTTON_DOESNT_WORK", "ON BIND viewHolderUserMessages")

        itemView.message_from_user.text = text
    }

}


