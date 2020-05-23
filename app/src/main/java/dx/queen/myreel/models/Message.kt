package dx.queen.myreel.models

data class Message(var id: String, var text: String, var fromUserId: String, var toCompanionId: String) {
    constructor():this("","","","")

}