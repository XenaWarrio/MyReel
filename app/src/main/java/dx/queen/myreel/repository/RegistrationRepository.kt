package dx.queen.myreel.repository

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dx.queen.myreel.models.FullUserInformation
import java.util.*

class RegistrationRepository(
    emailForSaving: String,
    passwordForSaving: String,
    username: String,
    imageUrl: String?,
    date: String
) {
    private val email = emailForSaving
    private val password = passwordForSaving
    private val userName = username
    private val urlImage = imageUrl
    private val dateOfBirth  = date
    private val defaultImageUri = "android.resource://dx.queen.myreel/drawable/voldemort"


    fun performRegister() {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                uploadImageToFirebaseStorage()
            }

            .addOnFailureListener {

            }

    }

    private fun uploadImageToFirebaseStorage() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        if (urlImage == null) {
            saveUserToFirebaseDatabase(defaultImageUri)
            return
        }

        ref.putFile(urlImage.toUri())
            .addOnSuccessListener {
                ref.downloadUrl
                    .addOnSuccessListener {
                        saveUserToFirebaseDatabase(it.toString())
                    }
            }
            .addOnFailureListener {
            }
    }

    private fun saveUserToFirebaseDatabase(imageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val refDataBase = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = FullUserInformation(
            uid,
            email,
            password,
            userName,
            imageUrl,
            dateOfBirth
        )

        refDataBase.setValue(user)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }

    }

}