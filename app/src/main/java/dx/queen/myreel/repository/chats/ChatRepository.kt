package dx.queen.myreel.repository.chats

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import dx.queen.myreel.models.Message
import dx.queen.myreel.viewModel.rvChat.ChatAdapter

class ChatRepository(private val companionId: String) {
   private val currentUserId = FirebaseAuth.getInstance().uid
   private val adapter = ChatAdapter()

    fun sendMessage(text: String) {
        Log.d("BUTTON_DOESNT_WORK", "repository")
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$currentUserId/$companionId")
            .push()
        val toRef =
            FirebaseDatabase.getInstance().getReference("/user-messages/$companionId/$currentUserId")
                .push()
//        val latestMessageRow =
//            FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
//        val latestMessageToRow =
//            FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")

        val message = Message(ref.key!!, text, currentUserId!!, companionId)
        Log.d("BUTTON_DOESNT_WORK", "  adapter userId = $currentUserId")

        adapter.addMessage(message)

        Log.d("BUTTON_DOESNT_WORK", "repository adapter list size = = ")

        ref.setValue(message)
        toRef.setValue(message)

/*        latestMessageRow.setValue(message)
        latestMessageToRow.setValue(message)*/
    }

    private fun listenForMessages() {
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$currentUserId/$companionId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(Message::class.java)
                if (message != null) {
                        adapter.addMessage(message)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {}

        })
        // rv_chat.scrollToPosition(adapter.itemCount - 1)
    }

}