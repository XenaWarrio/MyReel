package dx.queen.myreel.data_source.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dx.queen.myreel.models.FullUserInformationForDB

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: FullUserInformationForDB)

    @Delete
    fun delete(user: FullUserInformationForDB)

    @Query("SELECT * FROM full_user_information WHERE uid = :uid")
    fun getUser(uid: String): FullUserInformationForDB
}