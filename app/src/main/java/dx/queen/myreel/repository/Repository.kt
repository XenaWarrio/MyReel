package dx.queen.myreel.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class Repository {

    private val auth = FirebaseAuth.getInstance()

    var authFailedForRegistration = MutableLiveData<String>()
    var authHaveToConfirmEmailForRegistration = MutableLiveData<String>()

    var emailNotConfirmedForLogin = MutableLiveData<String>()
    var authFireBaseFailedForLogin = MutableLiveData<String>()
    var authSucceedForLogin = MutableLiveData<String>()




    fun createNewUser(
        emailForSaving: String,
        passwordForSaving: String,
        username: String,
        uri: String?,
        date: String
    ) {
        sendEmailVerification(emailForSaving, passwordForSaving)

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

    private fun sendEmailVerification(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                authHaveToConfirmEmailForRegistration.value = ""
                auth.currentUser!!.sendEmailVerification()
            }

            .addOnFailureListener {
               authFailedForRegistration.value = it.message
            }
    }

//    fun isEmailVerified() {
//        val user = FirebaseAuth.getInstance().currentUser
//        user!!.reload()
//        //isEmailConfirmed.value = user.isEmailVerified
//
//    }

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                if (!it.user!!.isEmailVerified) {
                    emailNotConfirmedForLogin.value = ""
                }else{
                    authSucceedForLogin.value = ""
                }
            }
            .addOnFailureListener {
                authFireBaseFailedForLogin.value = it.message
            }


    }
}