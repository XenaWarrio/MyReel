package dx.queen.myreel.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import dx.queen.myreel.models.FullUserInformationForFireBase
import dx.queen.myreel.viewModel.rvChats.ChatsItem

fun <T> MutableLiveData<ArrayList<T>>.addArrayList(values: List<T>) {
    val value = this.value ?: arrayListOf()
    value.addAll(values)
    this.value = value
}

class ChatsViewModel : ViewModel() {
    var chatItems: MutableLiveData<ArrayList<ChatsItem>> = MutableLiveData()
    var listOfChats = ArrayList<ChatsItem>()

    fun addNewChat(chat: ChatsItem) {
        listOfChats.add(chat)
        listOfChats.add(chat)
        listOfChats.add(chat)
        listOfChats.add(chat)
        listOfChats.add(chat)
        listOfChats.add(chat)
        listOfChats.add(chat)
        listOfChats.add(chat)
        listOfChats.add(chat)
        chatItems.addArrayList(listOfChats)
    }


     fun fetchCurrentUser() {
        val refDataBase =
            FirebaseDatabase.getInstance().getReference("/users/rE97mBGHV4YuAuPrFJsLlvcEOqL2")
        val user = FullUserInformationForFireBase(
            "rE97mBGHV4YuAuPrFJsLlvcEOqL2",
            "queen.de.xena@gmail.com",
            "0931547742k",
            "Xena",
            "android.resource://dx.queen.myreel/drawable/voldemort",
            "23.09.2015"
        )

        refDataBase.setValue(user)

        val chatItem = ChatsItem(
            "Xena",
            "android.resource://dx.queen.myreel/drawable/voldemort",
            "rE97mBGHV4YuAuPrFJsLlvcEOqL2"
        )
        addNewChat(chatItem)

  //      val ref = FirebaseDatabase.getInstance().getReference("/users/rE97mBGHV4YuAuPrFJsLlvcEOqL2")

//        ref.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                val exampleUser = p0.getValue(User::class.java)
//                val chatsItem
//            }
//
//        })
//
   }

}