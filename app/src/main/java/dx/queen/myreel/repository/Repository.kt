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

    private val workWithDatabase = WorkWithDatabase()

    //registration or login feedback
    var authFailedForRegistration = MutableLiveData<String>()
    var authHaveToConfirmEmailForRegistration = MutableLiveData<Unit>()
    var emailNotConfirmedForLogin = MutableLiveData<Unit>()
    var authFireBaseFailedForLogin = MutableLiveData<String>()
    var authSucceedForLogin = MutableLiveData<Unit>()

    //set observer that retrieve a message value by FireBase`s messageLiveData
    private val fireBase = FireBase().apply {
        messageLivaData.observeForever(messageObserver)
    }
    // observer that set message value to messageLiveData
    private val messageObserver = Observer<Message> {
        messageLiveData.value = it
    }
    var messageLiveData = MutableLiveData<Message>()

    //Registration logic
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

    //Login logic
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

    // LoginViewModel calls this method for getting user
    fun getUserInformationFromFireBase() = fireBase.getUserInformationFromFireBase()

    //LoginViewModel saves user that retrieve above to database
    suspend fun saveUserToDB(user: FullUserInformationForFireBase) {
        workWithDatabase.saveUserToDB(user)
    }

    //Chat logic
    fun sendMessage(message: String, companionEmail: String) {
        val fireBaseCompanion = FireBase(companionEmail)
        fireBaseCompanion.sendMessage(message)

    }
}