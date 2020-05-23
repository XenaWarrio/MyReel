package dx.queen.myreel.viewModel.rvChats

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatsItem(val name:String , val imageUrl: String , val companionId : String): Parcelable{
}
