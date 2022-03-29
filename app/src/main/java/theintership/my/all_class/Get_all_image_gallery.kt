package theintership.my.all_class

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import theintership.my.model.image

class get_all_image_gallery(mcontext: Context) {

    private val context = mcontext

    fun getAllImage(): MutableList<image> {
        var uri: Uri
        var cursor: Cursor?
        var colum_index_data: Int
        var list_string = mutableListOf<String>()
        var path: String
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val protec =
            arrayOf(MediaStore.MediaColumns.DATA , MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        cursor = context.contentResolver.query(uri, protec, null, null)
        colum_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        while (cursor.moveToNext()) {
            path = cursor.getString(colum_index_data)
            list_string.add(path)
        }
        var list2 : MutableList<image> = mutableListOf()
        for (i in 0 until list_string.size step 3){
            val id1 = i
            val id2 = i + 1
            val id3 = i + 2
            var path1 = list_string[id1]
            var path2 = ""
            var path3 = ""
            if (id2 < list_string.size){
                path2 = list_string[id2]
            }
            if (id3 < list_string.size){
                path3 = list_string[id3]
            }
            val image = image(path1 , path2 ,path3)
            list2.add(image)
        }
        return list2
    }


}