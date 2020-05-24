package dx.queen.myreel.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import dx.queen.myreel.data_source.local.WorkWithDatabase
import dx.queen.myreel.data_source.network.FireBase
import dx.queen.myreel.models.FullUserInformationForFireBase
import dx.queen.myreel.models.Message

class Repository {

    private val fireBase = FireBase().apply {
        messageLivaData.observeForever(messageObserver)
    }


    private val workWithDatabase = WorkWithDatabase()

    var authFailedForRegistration = MutableLiveData<String>()
    var authHaveToConfirmEmailForRegistration = MutableLiveData<Unit>()

    var emailNotConfirmedForLogin = MutableLiveData<Unit>()
    var authFireBaseFailedForLogin = MutableLiveData<String>()
    var authSucceedForLogin = MutableLiveData<Unit>()

    private val messageObserver = Observer<Message> {
        messageLiveData.value = it
    }

    var messageLiveData = MutableLiveData<Message>()

    fun createNewUserRepository(
        emailForSaving: String,
        passwordForSaving: String,
        username: String,
        uri: String?,
        date: String
    ) {
        val firebaseSourceData = FireBase(emailForSaving, passwordForSaving, username, uri, date)
        firebaseSourceData.createNewUser()
        fireBase.authHaveToConfirmEmailForRegistration.observeForever {
            authHaveToConfirmEmailForRegistration.value = Unit
        }

        fireBase.authFailedForRegistration.observeForever {
            authFailedForRegistration.value = it
        }
    }

    fun signIn(email: String, password: String) {
        fireBase.signIn(email, password)

        fireBase.emailNotConfirmedForLogin.observeForever {
            emailNotConfirmedForLogin.value = Unit
        }
        fireBase.authSucceedForLogin.observeForever {
            authSucceedForLogin.value = Unit
        }

        fireBase.authFireBaseFailedForLogin.observeForever {
            authFireBaseFailedForLogin.value = it
        }
    }

    fun getUserInformationFromFireBase() = fireBase.getUserInformationFromFireBase()

    suspend fun saveUserToDB(user: FullUserInformationForFireBase) {
        workWithDatabase.saveUserToDB(user)
    }

    fun sendMessage(message: String, companionEmail: String) {
        val fireBaseCompanion = FireBase(companionEmail)
        fireBaseCompanion.sendMessage(message)

    }
}