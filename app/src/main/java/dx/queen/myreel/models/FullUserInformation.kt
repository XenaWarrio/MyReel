package dx.queen.myreel.models

data class FullUserInformation(
    var uid: String,
    var email: String,
    var password: String,
    var userName: String,
    var imageUrl: String,
    var dateOfBirth: String
) {

    constructor(): this("","","","","","")
}