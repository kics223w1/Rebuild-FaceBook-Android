package theintership.my.signin_signup

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import theintership.my.MyMethod.Companion.showToastShort
import theintership.my.R
import theintership.my.get_all_image_gallery
import theintership.my.model.image
import theintership.my.signin_signup.adapter.IClickImage
import theintership.my.signin_signup.adapter.adapter_image

class TestActivity : AppCompatActivity()  , IClickImage {


    override fun onCreate(savedInstanceState: Bundle?){
        setContentView(R.layout.test_activity)
        super.onCreate(savedInstanceState)

//        val rcv= findViewById<RecyclerView>(R.id.rcv_test_activity)
//        val list_image = get_all_image_gallery(this).getAllImage()
//
//        val linearLayout : RecyclerView.LayoutManager = LinearLayoutManager(this)
//        val adapter = adapter_image(this, this)
//        adapter.submitList(list_image)
//
//        rcv.layoutManager = linearLayout
//        rcv.adapter = adapter



    }

    override fun onClickImage(path: String) {
        val s = path
        s.showToastShort(this)
    }

}