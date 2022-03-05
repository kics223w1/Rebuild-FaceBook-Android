package theintership.my.model

import androidx.room.PrimaryKey

data class User(
    @PrimaryKey(autoGenerate = true)
    var email : String? = null,
    var fullname : String? = null ,
    var phone : String? = null,
    var sex : String? = null ,
    var pronoun : String? = null,
    var gender : String? = null,
    var age : Int,
    var birthday : String? = null
) {

}