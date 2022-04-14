package theintership.my.signin_signup.model

import androidx.room.PrimaryKey


data class Language(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String
) {

}