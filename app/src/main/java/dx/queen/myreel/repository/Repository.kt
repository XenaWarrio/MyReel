package dx.queen.myreel.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class Repository {

    val auth = FirebaseAuth.getInstance()


    var authError = MutableLiveData<String>()
    var authConfirmEmail = MutableLiveData<String>()

    var isEmailConfirmed = MutableLiveData<Boolean>()


    fun createNewUser(
        emailForSaving: String,
        passwordForSaving: String,
        username: String,
        uri: String?,
        date: String
    ) {
        verifyEmail(emailForSaving, passwordForSaving)

//        val registration =
//            RegistrationRepository(
//                emailForSaving,
//                passwordForSaving,
//                username,
//                uri,
//                date
//            )
//        registration.performRegister()
    }

    private fun verifyEmail(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                authConfirmEmail.value = ""
                auth.currentUser!!.sendEmailVerification()

            }

            .addOnFailureListener {
                authError.value = it.message
            }
    }

    fun isEmailVerified(){
       isEmailConfirmed.value = auth.currentUser!!.isEmailVerified
    }
}