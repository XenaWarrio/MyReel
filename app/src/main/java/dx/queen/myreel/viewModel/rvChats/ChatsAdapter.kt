package dx.queen.myreel.viewModel.rvChats

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dx.queen.myreel.R
import dx.queen.myreel.view.ChatsFragment
import kotlinx.android.synthetic.main.view_lower_chats.view.*

class ChatsAdapter() :
    RecyclerView.Adapter<ChatsViewHolder>() {

    val clickOnItem = MutableLiveData<ChatsItem>()
    private var chatsList = ArrayList<ChatsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_lower_chats, parent, false)
        return ChatsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatsList.size
    }

    fun updateList(chatsItemList: ArrayList<ChatsItem>) {
        chatsList = chatsItemList
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            clickOnItem.value = chatsList[position]
        }
        holder.bind(chatsList[position])
    }
}

class ChatsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(chatsItem: ChatsItem) {
        itemView.username.text = chatsItem.name
        Picasso.get().load(chatsItem.imageUrl).into(itemView.image_chats_row)
    }

}