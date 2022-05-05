package theintership.my.signin_signup.model

import androidx.room.PrimaryKey

data class Email_Account(
    @PrimaryKey(autoGenerate = true)
    var email : String? = null,
    var account : String? = null
) {
}