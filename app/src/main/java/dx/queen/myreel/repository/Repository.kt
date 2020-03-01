package dx.queen.myreel.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class Repository {

    var authError = MutableLiveData<String>()

    fun createNewUser(
        emailForSaving: String,
        passwordForSaving: String,
        username: String,
        uri: String?,
        date: String
    ) {

        val registration =
            RegistrationRepository(
                emailForSaving,
                passwordForSaving,
                username,
                uri,
                date
            )
        registration.performRegister()
    }

    private fun verifyEmail(email: String , password : String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {

             }

            .addOnFailureListener {
                authError.value = it.message
            }

    }
}