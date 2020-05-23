package dx.queen.myreel.data_source.local.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dx.queen.myreel.appInstance.AppInstance
import dx.queen.myreel.models.FullUserInformationForDB

@Database(entities = [FullUserInformationForDB::class], version = 1, exportSchema = false)

abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(): UserDatabase? {
            if (INSTANCE == null) {
                synchronized(UserDatabase::class.java) {
                    if (INSTANCE == null)
                    INSTANCE = Room.databaseBuilder(
                            AppInstance.instance.applicationContext,
                            UserDatabase::class.java,
                            "database"
                        )
                            .build()
                }
            }
            return INSTANCE
        }
    }

}