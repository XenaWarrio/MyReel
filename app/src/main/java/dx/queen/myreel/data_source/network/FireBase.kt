package dx.queen.myreel.data_source.network

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import dx.queen.myreel.appInstance.AppInstance
import dx.queen.myreel.models.FullUserInformationForFireBase
import dx.queen.myreel.models.Message
import dx.queen.myreel.view.rememberUser.SharedPreferencesIsUserRegister
import dx.queen.myreel.view.rememberUser.SharedPreferencesIsUserRegister.getUserId
import dx.queen.myreel.view.rememberUser.SharedPreferencesIsUserRegister.writeUserId

class FireBase() {
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private var urlImage: String? = null
    private lateinit var dateOfBirth: String
    private val defaultImageUri = "android.resource://dx.queen.myreel/drawable/voldemort"

    private val auth = FirebaseAuth.getInstance()
    private val context = AppInstance.instance.applicationContext

    var authFailedForRegistration = MutableLiveData<String>()
    var authFireBaseFailedForLogin = MutableLiveData<String>()

    var authHaveToConfirmEmailForRegistration = MutableLiveData<Unit>()
    var emailNotConfirmedForLogin = MutableLiveData<Unit>()
    var authSucceedForLogin = MutableLiveData<Unit>()

    private lateinit var companionId: String

    constructor(companionUserId: String):this(){
        companionId = companionUserId
    }
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
    }


    private fun register() {
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
        ref.putFile(urlImage!!.toUri())
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


    fun createNewUser() {
        sendEmailVerification(email, password)
        register()
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

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                if (!it.user!!.isEmailVerified) {
                    emailNotConfirmedForLogin.value = Unit
                } else {
                    SharedPreferencesIsUserRegister.init(context).apply {
                        writeUserId(it.user!!.uid)
                    }
                    authSucceedForLogin.value = Unit
                }
            }
            .addOnFailureListener {
                authFireBaseFailedForLogin.value = it.message
            }
    }

private fun getCurrentUserId():String{
    return SharedPreferencesIsUserRegister.init(context).let{
        getUserId()!!
    }
}

    fun getUserInformationFromFireBase(): FullUserInformationForFireBase? {
        auth.currentUser!!.reload()
        var user: FullUserInformationForFireBase? = null
        val userId = getCurrentUserId()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$userId")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                user = p0.getValue(FullUserInformationForFireBase::class.java)
            }
        })
        return user
    }

    var messageLivaData =  MutableLiveData<Message>()


    fun sendMessage(text: String) {
        val currentUserId = getCurrentUserId()

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$currentUserId/$companionId")
            .push()
        val toRef =
            FirebaseDatabase.getInstance().getReference("/user-messages/$companionId/$currentUserId")
                .push()
//        val latestMessageRow =
//            FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
//        val latestMessageToRow =
//            FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")

        val message = Message(ref.key!!, text, currentUserId!!, companionId)
        Log.d("BUTTON_DOESNT_WORK", "  adapter userId = $currentUserId")

     //   adapter.addMessage(message)
        this.messageLivaData.value = message

        Log.d("BUTTON_DOESNT_WORK", "repository adapter list size = = ")

        ref.setValue(message)
        toRef.setValue(message)

/*        latestMessageRow.setValue(message)
        latestMessageToRow.setValue(message)*/
    }

    private fun listenForMessages() {
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/${getCurrentUserId()}/$companionId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(Message::class.java)
                if (message != null) {
                    //adapter.addMessage(message)
                    messageLivaData.value = message
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {}

        })
        // rv_chat.scrollToPosition(adapter.itemCount - 1)
    }
}