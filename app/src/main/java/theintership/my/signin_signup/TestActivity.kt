package theintership.my.signin_signup

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.R

class TestActivity : AppCompatActivity()   {

    private val storage = Firebase.storage("gs://the-intership.appspot.com")

    override fun onCreate(savedInstanceState: Bundle?){
        setContentView(R.layout.test_activity)
        super.onCreate(savedInstanceState)
        var image = findViewById<ImageView>(R.id.imageViewTest)

        var storageRef = storage.reference



    }

}