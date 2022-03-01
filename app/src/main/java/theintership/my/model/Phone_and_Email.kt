package theintership.my.model

import androidx.room.PrimaryKey

data class Phone_and_Email(
    @PrimaryKey(autoGenerate = true)
    val id : Int ,
    var phone : String? = null,
    var email : String? = null
) {
}