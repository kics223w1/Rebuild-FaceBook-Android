package theintership.my.model

import androidx.room.PrimaryKey

data class Phone_and_Email(
    @PrimaryKey()
    var email : String? = null,
    var phone : String? = null
) {
}