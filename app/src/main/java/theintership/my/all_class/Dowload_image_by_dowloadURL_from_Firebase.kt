package theintership.my.all_class

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class Dowload_image_from_Firebase_by_dowloadURL{
    private val storage = Firebase.storage("gs://the-intership.appspot.com")
    private val storageRef = storage.reference

    fun dowload( path_ref : String) : Task<Uri> {
        return storageRef.child(path_ref).downloadUrl
    }
}