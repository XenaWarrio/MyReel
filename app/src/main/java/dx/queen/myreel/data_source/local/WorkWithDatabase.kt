package dx.queen.myreel.data_source.local

import dx.queen.myreel.models.FullUserInformationForDB
import dx.queen.myreel.models.FullUserInformationForFireBase
import dx.queen.myreel.data_source.local.db.UserDatabase

class WorkWithDatabase {

    private val userDatabase = UserDatabase.getDatabase()!!.userDao()

    /*
  save user to database after his email is verified
  * */
    suspend fun saveUserToDB(user: FullUserInformationForFireBase) {
        val userForDb = FullUserInformationForDB(
            0,
            user.uid,
            user.email,
            user.password,
            user.userName,
            user.imageUrl,
            user.dateOfBirth
        )
            userDatabase.insert(userForDb)
    }
}