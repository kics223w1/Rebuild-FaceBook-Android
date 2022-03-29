package theintership.my.all_class

import android.graphics.Bitmap
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class upload_image_by_putBytes_to_firebase {
    private val storage = Firebase.storage("gs://the-intership.appspot.com")
    var storageRef = storage.reference
    fun upload(bitmap: Bitmap, path_ref: String): UploadTask {
        val ref = storageRef.child(path_ref)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val data = baos.toByteArray()
        return ref.putBytes(data)
    }

}