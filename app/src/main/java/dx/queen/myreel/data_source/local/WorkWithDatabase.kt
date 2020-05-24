package dx.queen.myreel.data_source.local

import dx.queen.myreel.models.FullUserInformationForDB
import dx.queen.myreel.models.FullUserInformationForFireBase
import dx.queen.myreel.data_source.local.db.UserDatabase

class WorkWithDatabase {

    private val userDatabase = UserDatabase.getDatabase()!!.userDao()

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

//    suspend fun get(uid: String): FullUserInformationForFireBase {
//        return suspendCoroutine {
//            val result = userDatabase.getUser(uid)
//            it.resume(result)
//        }
//    }


//    suspend fun getUserByUid(uid: String): FullUserInformationForFireBase {
//
//        val job = GlobalScope.async {
//            Log.d(
//                "COROUTINE_SEE",
//                "get user from db , user = $user"
//            )
//            userDatabase.getUser(uid)
//            //get(uid)
//        }
//        Log.d(
//            "COROUTINE_SEE",
//            "user = $user + ${job.getCompleted()}"
//        )
//        user = job.await()
//
//        Log.d(
//            "COROUTINE_SEE",
//            "user = $user"
//        )
//
//        return FullUserInformationForFireBase(
//            user.uid,
//            user.email,
//            user.password,
//            user.userName,
//            user.imageUrl,
//            user.dateOfBirth
//        )
//    }
}