package theintership.my.signin_signup

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import theintership.my.R
import theintership.my.get_all_image_gallery

class TestActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?){
        setContentView(R.layout.test_activity)
        super.onCreate(savedInstanceState)

        val btn = findViewById<Button>(R.id.btn_get_image)
        val grid_view = findViewById<GridView>(R.id.gridview_test)


        btn.setOnClickListener{
            grid_view.visibility = View.VISIBLE
            btn.visibility = View.INVISIBLE
            var adapter = base_adapter_image_gallery(this)
            val list = get_all_image_gallery(this).getAllImage()
            println("debug size: ${list.size}")
            println("debug list: $list")
            adapter.setData(list)
            grid_view.adapter = adapter
        }

    }

}