package theintership.my.all_class

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.File

class upload_image_by_putFile_to_firebase {
    private val storage = Firebase.storage("gs://the-intership.appspot.com")
    private val storageRef = storage.reference
    fun upload(path_image: String, path_ref: String): UploadTask {
        val file = Uri.fromFile(File(path_image))
        val riversRef = storageRef.child(path_ref)

        return riversRef.putFile(file)
    }

}