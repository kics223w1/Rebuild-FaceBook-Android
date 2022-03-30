package theintership.my.model

import android.graphics.Bitmap
import androidx.room.PrimaryKey

data class category_privacy_avatar(
    @PrimaryKey
    val name : String?,
    val image : Int //R.drawable.
) {
}