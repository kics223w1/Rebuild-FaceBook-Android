package theintership.my.signin_signup

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

class get_all_image_gallery(mcontext: Context) {

    private val context = mcontext

    fun getAllImage(): MutableList<String> {
        var uri: Uri
        var cursor: Cursor?
        var colum_index_data: Int
        var list = mutableListOf<String>()
        var path: String
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val protec =
            arrayOf(MediaStore.MediaColumns.DATA , MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        cursor = context.contentResolver.query(uri, protec, null, null)
        colum_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        while (cursor.moveToNext()) {
            path = cursor.getString(colum_index_data)
            list.add(path)
        }
        return list
    }


}