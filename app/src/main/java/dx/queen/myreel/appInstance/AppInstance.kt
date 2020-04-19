package dx.queen.myreel.appInstance

import android.app.Application

class AppInstance : Application() {
    companion object {
        lateinit var instance: AppInstance
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}