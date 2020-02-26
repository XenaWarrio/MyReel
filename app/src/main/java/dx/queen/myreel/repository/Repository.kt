package dx.queen.myreel.repository

class Repository {

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
}