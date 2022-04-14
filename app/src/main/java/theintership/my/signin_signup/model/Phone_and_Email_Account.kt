package theintership.my.signin_signup.model

import androidx.room.PrimaryKey
import java.util.*

data class Phone_and_Email_Account(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var email : String? = null,
    var phone : String? = null,
    var account : String? = null
) {
}