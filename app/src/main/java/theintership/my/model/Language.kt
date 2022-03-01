package theintership.my.model

import androidx.room.PrimaryKey


data class Language(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String
) {

}