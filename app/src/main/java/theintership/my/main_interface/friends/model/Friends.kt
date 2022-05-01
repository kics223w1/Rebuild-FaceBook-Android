package theintership.my.main_interface.friends.model

import androidx.room.PrimaryKey

data class Friends(
    val name : String?,
    val account_ref : String?,
    val link_avatar : String?,
    val day : String?,
    val hour : String?
) {

}