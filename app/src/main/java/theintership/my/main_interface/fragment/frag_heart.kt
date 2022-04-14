package theintership.my.main_interface.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.MainActivity
import theintership.my.Main_Interface_Activity
import theintership.my.R
import theintership.my.databinding.FragHeartBinding


class frag_heart : Fragment() {


    private var database: DatabaseReference = Firebase.database.reference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.frag_heart , container ,false)
        val tv = view.findViewById<TextView>(R.id.tv_frag_heart)
        val ref = database.child("ok")

        tv.setOnClickListener{
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            activity?.finish()
        }


        return view
    }

}
