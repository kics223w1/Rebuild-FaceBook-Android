package theintership.my.model

import androidx.room.PrimaryKey

data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var fullname : String? = null ,
    var phone : String? = null,
    var email : String? = null,
    var sex : String? = null ,
    var pronoun : String? = null,
    var age : Int,
    var birthday : String? = null
) {

}