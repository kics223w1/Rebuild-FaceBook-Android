package theintership.my.model

import androidx.room.PrimaryKey


data class language(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String
) {

}