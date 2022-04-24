package theintership.my.main_interface.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.Main_Interface_Activity
import theintership.my.R
import theintership.my.all_class.MyMethod
import theintership.my.databinding.FragGroupBinding
import theintership.my.main_interface.notifications.model.Notifications


class frag_group : Fragment() {


    private lateinit var database: DatabaseReference
    private lateinit var account_ref : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_group , container ,false)
        val btn = view.findViewById<TextView>(R.id.btn_frag_group)
        val edt = view.findViewById<EditText>(R.id.edt_frag_group)
        database = Firebase.database.reference
        val sharedPref = context?.applicationContext?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        account_ref = sharedPref?.getString("account_ref" , "").toString()
        btn.setOnClickListener {
            if (edt.text.toString() == ""){
                return@setOnClickListener
            }
            add_noti(edt.text.toString())
        }
        return view
    }

    private fun add_noti(s : String){
        var id = s.toInt()
        val ref = database
            .child("User")
            .child(account_ref)
            .child("notifications")
        val noti1 = Notifications(
            to_person = "Huy44",
            "",
            day_create = "04/16/2022",
            from_person = "Cao Viet Huy",
            content = "Cao Viet Huy da thich bai viet cua ban trong Hoi Lap trinh Android",
            group = "Hoi Lap trinh Android",
            icon = "love love",
            kind_of_noti = "Comment",
            link_avatar_person = "https://firebasestorage.googleapis.com/v0/b/the-intership.appspot.com/o/avatar_user%2Fhuy1?alt=media&token=52d4b2e6-1a74-4ee5-ad19-51d9db2eead5",
            is_readed = false,
            link_post = "Link Post ne",
            id_comment = 22
        )

        noti1.set_day_and_time()
        noti1.set_day_create()
        ref.child(id.toString()).setValue(noti1)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    println("debug add success")
                } else {
                    println("debug add fail")
                }
            }
    }

}