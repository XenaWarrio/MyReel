package dx.queen.myreel.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dx.queen.myreel.models.FullUserInformationForFireBase

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

        val registration =
            RegistrationRepository(
                emailForSaving,
                passwordForSaving,
                username,
                uri,
                date
            )
        registration.register()
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


    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                if (!it.user!!.isEmailVerified) {
                    emailNotConfirmedForLogin.value = ""
                } else {
                    authSucceedForLogin.value = ""
                }
            }
            .addOnFailureListener {
                authFireBaseFailedForLogin.value = it.message
            }

    }

    fun getUserInformationFromFireBase(): FullUserInformationForFireBase? {
        auth.currentUser!!.reload()
        var user : FullUserInformationForFireBase? = null
        val userId = auth.currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$userId")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                user = p0.getValue(FullUserInformationForFireBase::class.java)
            }

        })
        return user
    }
}