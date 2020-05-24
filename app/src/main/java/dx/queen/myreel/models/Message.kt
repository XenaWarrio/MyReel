package dx.queen.myreel.models

data class Message(var id: String, var text: String, var fromUser: String, var toUser: String) {
    constructor():this("","","","")

}