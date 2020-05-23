package dx.queen.myreel.view.rememberUser

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesIsUserRegister {
    private lateinit var prefs: SharedPreferences
    private const val APP_SETTINGS = "APP_SETTINGS"
    private const val USER_ID = "user id"

    fun init(context: Context) {
        prefs = context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    }

    fun writeUserId(userID: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(USER_ID, userID)
            commit()
        }
    }

    fun getUserId() = prefs.getString(USER_ID, "")!!

}