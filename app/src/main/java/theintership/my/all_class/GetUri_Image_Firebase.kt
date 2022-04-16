package theintership.my.all_class

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class GetUri_Image_Firebase {
    private val storage = Firebase.storage("gs://the-intership.appspot.com")
    var storageRef = storage.reference
    fun getUri(path_ref: String): Task<Uri> {
        return storageRef.child(path_ref).downloadUrl
    }

}