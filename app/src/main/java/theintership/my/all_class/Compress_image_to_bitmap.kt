package theintership.my.all_class

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Compress_image_to_bitmap {
    fun compress(image_path : String) :Bitmap {
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap = BitmapFactory.decodeFile(image_path, options)
        return bitmap
    }
}