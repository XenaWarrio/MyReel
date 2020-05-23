package dx.queen.myreel.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class FullUserInformationForFireBase(
    var uid: String,
    var email: String,
    var password: String,
    var userName: String,
    var imageUrl: String,
    var dateOfBirth: String
) {

    constructor() : this("", "", "", "", "", "")
}

@Entity(tableName = "full_user_information")
data class FullUserInformationForDB(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var uid: String,
    var email: String,
    var password: String,
    var userName: String,
    var imageUrl: String,
    var dateOfBirth: String
)

