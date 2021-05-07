package dx.queen.myreel.view.rememberUser

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesIsUserRegister {
    private lateinit var prefs: SharedPreferences
    private const val APP_SETTINGS = "APP_SETTINGS"
    private const val USER_EMAIL = "user email"

    fun init(context: Context) {
        prefs = context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    }

    fun writeUserEmail(userID: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(USER_EMAIL, userID)
            commit()
        }
    }

    fun getUserEmail(): String? = prefs.getString(USER_EMAIL, "")

}