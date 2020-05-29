package dx.queen.myreel.data_source.network

import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import dx.queen.myreel.appInstance.AppInstance
import dx.queen.myreel.models.FullUserInformationForFireBase
import dx.queen.myreel.models.Message
import dx.queen.myreel.utils.clearPoints
import dx.queen.myreel.view.rememberUser.SharedPreferencesIsUserRegister
import dx.queen.myreel.view.rememberUser.SharedPreferencesIsUserRegister.getUserEmail
import dx.queen.myreel.view.rememberUser.SharedPreferencesIsUserRegister.writeUserEmail

class FireBase() {
    //for registration

    private lateinit var email: String
    private lateinit var formattedUserEmail : String // Firebase Database paths must not contain '.', '#', '$', '[', or ']'
    private lateinit var password: String
    private lateinit var userName: String
    private var urlImage: String? = null
    private lateinit var dateOfBirth: String
    private val defaultImageUri = "android.resource://dx.queen.myreel/drawable/voldemort"

    constructor(
        emailForSaving: String,
        passwordForSaving: String,
        username: String,
        imageUrl: String?,
        date: String
    ) : this() {
        email = emailForSaving
        password = passwordForSaving
        userName = username
        dateOfBirth = date
        urlImage = imageUrl
        formattedUserEmail = email.clearPoints()
    }

    //registration or login feedback
    var authFailedForRegistration = MutableLiveData<String>()
    var authFireBaseFailedForLogin = MutableLiveData<String>()
    var authHaveToConfirmEmailForRegistration = MutableLiveData<Unit>()
    var emailNotConfirmedForLogin = MutableLiveData<Unit>()
    var authSucceedForLogin = MutableLiveData<Unit>()

    private val auth = FirebaseAuth.getInstance()
    private val context = AppInstance.instance.applicationContext

    // companionEmail is for messaging between users
    private lateinit var companionEmail: String
    constructor(companionUserEmail: String) : this() {
        companionEmail = companionUserEmail
    }

    //Registration logic
    fun createNewUser() {
        sendEmailVerification(email, password)
        register()
    }

    //register user
    private fun register() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                authFailedForRegistration.value = it.message
            }

    }

    //upload user`s image to FirebaseStorage
    private fun uploadImageToFirebaseStorage() {
        val ref = FirebaseStorage.getInstance().getReference("/images/$formattedUserEmail")

        if (urlImage == null) { // if user didn`t choose a photo
            ref.putFile(defaultImageUri.toUri())
                .addOnSuccessListener {
                    ref.downloadUrl
                        .addOnSuccessListener {
                            saveUserToFirebaseDatabase(it.toString()) //after default imageUrl was successfully download we are save full user information
                        }
                }
            return
        }

        ref.putFile(urlImage!!.toUri())
            .addOnSuccessListener {
                ref.downloadUrl
                    .addOnSuccessListener {
                        saveUserToFirebaseDatabase(it.toString())//after imageUrl was successfully download we are save full user information
                    }
            }

    }

    private fun saveUserToFirebaseDatabase(imageUrl: String) {
        val refDataBase = FirebaseDatabase.getInstance().getReference("/users/$formattedUserEmail")
        val user = FullUserInformationForFireBase(
            FirebaseAuth.getInstance().currentUser!!.uid,
            email,
            password,
            userName,
            imageUrl,
            dateOfBirth
        )

        refDataBase.setValue(user)
            .addOnSuccessListener {}
            .addOnFailureListener {}
    }


    private fun sendEmailVerification(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                authHaveToConfirmEmailForRegistration.value = Unit
                auth.currentUser!!.sendEmailVerification()
            }
            .addOnFailureListener {
                authFailedForRegistration.value = it.message
            }
    }

    //Login logic
    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                if (it.user == null) return@addOnSuccessListener
                if (!it.user!!.isEmailVerified) {
                    emailNotConfirmedForLogin.value = Unit
                } else {
                    SharedPreferencesIsUserRegister.init(context).apply {
                        writeUserEmail(email.clearPoints()) // for remember user key -> email
                    }
                    authSucceedForLogin.value = Unit
                }
            }
            .addOnFailureListener {
                authFireBaseFailedForLogin.value = it.message
            }
    }

    private fun getCurrentUserEmail(): String {
        return SharedPreferencesIsUserRegister.init(context).let {
            getUserEmail()!!
        }
    }

    // the repository calls this method after user verify his email for saving user to local database
    fun getUserInformationFromFireBase(): FullUserInformationForFireBase? {
        auth.currentUser!!.reload() // update currentUser EmailIsVerified

        var user: FullUserInformationForFireBase? = null
        val userEmail = getCurrentUserEmail()
        val ref = FirebaseDatabase.getInstance().getReference("/users/${userEmail.clearPoints()}")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                user = p0.getValue(FullUserInformationForFireBase::class.java)
            }
        })
        return user
    }

    var messageLivaData = MutableLiveData<Message>()


    //Chat logic
    fun sendMessage(text: String) {
        val currentUserEmail = getCurrentUserEmail().clearPoints()

        val ref = FirebaseDatabase.getInstance()
            .getReference("/user-messages/$currentUserEmail/$companionEmail")
            .push()
        val toRef =
            FirebaseDatabase.getInstance()
                .getReference("/user-messages/$companionEmail/$currentUserEmail")
                .push()
//        val latestMessageRow =
//            FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
//        val latestMessageToRow =
//            FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")

        val message = Message(ref.key!!, text, currentUserEmail, companionEmail)

        this.messageLivaData.value = message // set message that should be displayed on the screen

        ref.setValue(message)
        toRef.setValue(message)

        listenForMessages()
/*        latestMessageRow.setValue(message)
        latestMessageToRow.setValue(message)*/
    }

    private fun listenForMessages() {
        val ref = FirebaseDatabase.getInstance()
            .getReference("/user-messages/${getCurrentUserEmail()}/$companionEmail")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(Message::class.java)
                if (message != null) {
                    messageLivaData.value = message
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {}

        })
        // rv_chat.scrollToPosition(adapter.itemCount - 1)
    }
}