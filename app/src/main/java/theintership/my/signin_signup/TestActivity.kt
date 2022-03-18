package theintership.my.signin_signup

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import theintership.my.R

class TestActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?){
        setContentView(R.layout.test_activity)
        super.onCreate(savedInstanceState)

        val btn = findViewById<Button>(R.id.btn_get_image)
        val rcv = findViewById<RecyclerView>(R.id.rcv_test)

        btn.setOnClickListener{
            rcv.visibility = View.VISIBLE
            btn.visibility = View.INVISIBLE
            val list = get_all_image_gallery(this).getAllImage()
            val adapter = adapter_image(this)
            adapter.setData(list)
            val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
            rcv.layoutManager = layoutManager
            rcv.adapter = adapter
        }

    }

}