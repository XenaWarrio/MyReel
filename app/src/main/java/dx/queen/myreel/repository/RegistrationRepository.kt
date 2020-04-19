package dx.queen.myreel.repository

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dx.queen.myreel.models.FullUserInformationForFireBase

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
    private val dateOfBirth = date
    private val defaultImageUri = "android.resource://dx.queen.myreel/drawable/voldemort"


    fun register() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                uploadImageToFireBaseStorage()
            }
            .addOnFailureListener {
            }

    }

    private fun uploadImageToFireBaseStorage() {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseStorage.getInstance().getReference("/images/$userID")

        if (urlImage == null) {
            ref.putFile(defaultImageUri.toUri())
                .addOnSuccessListener {
                    ref.downloadUrl
                        .addOnSuccessListener {
                            saveUserToFireBaseDatabase(it.toString(), userID)
                        }
                }
            return
        }

        ref.putFile(urlImage.toUri())
            .addOnSuccessListener {
                ref.downloadUrl
                    .addOnSuccessListener {
                        saveUserToFireBaseDatabase(it.toString(), userID)
                    }
            }

    }

    private fun saveUserToFireBaseDatabase(imageUrl: String, userId: String) {
        val refDataBase = FirebaseDatabase.getInstance().getReference("/users/$userId")

        val user = FullUserInformationForFireBase(
            userId,
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