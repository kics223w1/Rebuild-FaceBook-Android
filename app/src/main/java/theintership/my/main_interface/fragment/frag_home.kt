package theintership.my.main_interface.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import theintership.my.R
import theintership.my.Main_Interface_Activity
import theintership.my.all_class.MyMethod.Companion.blackout_char
import theintership.my.all_class.MyMethod.Companion.count_days
import theintership.my.all_class.MyMethod.Companion.set_today
import theintership.my.all_class.MyMethod.Companion.showToastLong
import theintership.my.all_class.get_all_image_gallery
import java.text.SimpleDateFormat
import java.util.*


class frag_home : Fragment() {

    private lateinit var mainInterfaceActivity: Main_Interface_Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_home , container ,false)
        val textView = view.findViewById<TextView>(R.id.tv_frag_home)
        mainInterfaceActivity = activity as Main_Interface_Activity

        val tv2 = "Cao Viet Huy da thich bai viet cua ban trong hoi lap trinh android vao ngay hom truoc kia do"
        val list = mutableListOf<String>()
        list.add("hoi lap trinh android")
        list.add("Cao Viet Huy")
        val span = blackout_char(tv2 , list)
        textView.text = tv2
        val aa = textView.text.toString()
        textView.setText(span)
        val cnt =count_days("04/12/2022" , "04/14/2022");
        if (cnt >= 0){
            println("debug cnt>=0")
        }
        println("debug ${count_days("04/12/2022" , "04/14/2022")}")
        println("debug today: ${set_today()}")


        return view
    }

}
