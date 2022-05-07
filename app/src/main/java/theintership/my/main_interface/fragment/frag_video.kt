package theintership.my.main_interface.fragment

import android.content.Context
import android.content.Intent
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
import kotlinx.android.synthetic.main.frag_video.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import theintership.my.MainActivity
import theintership.my.Main_Interface_Activity
import theintership.my.R
import theintership.my.all_class.MyMethod
import theintership.my.all_class.MyMethod.Companion.get_hour
import theintership.my.all_class.MyMethod.Companion.set_today
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.databinding.FragVideoBinding
import theintership.my.main_interface.friends.model.Friends
import theintership.my.main_interface.notifications.model.Notifications
import theintership.my.main_interface.profile.frag_profile_other


class frag_video : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var account_ref: String
    private lateinit var mainInterfaceActivity: Main_Interface_Activity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_video, container, false)
        val btn_noti = view.findViewById<TextView>(R.id.btn_add_noti)
        val btn_friends = view.findViewById<TextView>(R.id.btn_add_friends)
        val edt_id = view.findViewById<EditText>(R.id.edt_id)
        val edt_icon = view.findViewById<EditText>(R.id.edt_icon)
        val edt_content = view.findViewById<EditText>(R.id.edt_content)
        val edt_kind_of_noti = view.findViewById<EditText>(R.id.edt_kind_of_notifications)
        val edt_friends = view.findViewById<EditText>(R.id.edt_friends)
        val edt_id_friends = view.findViewById<EditText>(R.id.edt_id_friends)
        val btn_back_sign_in = view.findViewById<TextView>(R.id.btn_back_sign_in)
        mainInterfaceActivity = activity as Main_Interface_Activity
        database = Firebase.database.reference
        val sharedPref = context?.applicationContext?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        account_ref = sharedPref?.getString("account ref", "").toString()

        btn_friends.setOnClickListener {
            if (edt_friends.text.toString() == "" ){
                return@setOnClickListener
            }
            go_to_frag_profile_others(from = account_ref ,to = edt_friends.text.toString())
//            val account_ref_owner = sharedPref?.getString("account ref" , "")
//            add_friends(account_ref_owner.toString() , edt_friends.text.toString())
        }

        btn_back_sign_in.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            activity?.finish()
        }

        btn_noti.setOnClickListener {
            if (edt_kind_of_noti.text.toString() == "" || edt_id.text.toString() == "" || edt_icon.text.toString() == "" || edt_content.text.toString() == "") {
                return@setOnClickListener
            }
            add_noti(
                edt_id.text.toString(),
                edt_icon.text.toString(),
                edt_content.text.toString(),
                edt_kind_of_noti.text.toString()
            )
        }
        return view
    }


    private fun go_to_frag_profile_others(from: String , to : String) {
        val arg = Bundle()
        arg.putString("account ref from", from)
        arg.putString("account ref to" , to)
        MyMethod.replacefrag_in_main_interface_with_bundle(
            "frag_profile_others",
            frag_profile_other(),
            mainInterfaceActivity.supportFragmentManager,
            arg
        )
    }

    private fun add_friends(account_ref_owner : String , account_ref_fr: String){
        val ref = database
            .child("User")
            .child(account_ref_fr)
            .child("friends")
            .child("request")
            .child(account_ref_owner)
        val sharedPref = context?.applicationContext?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val link_avatar= sharedPref?.getString("link avatar" , "link avatar == null").toString()
        val user_name =  sharedPref?.getString("user name" , "name == null").toString()
        val fr = Friends(user_name,account_ref_owner , link_avatar , set_today() , get_hour())
        ref.setValue(fr)
    }

    private fun add_noti(id: String, icon: String, content: String, kind_of_noti: String) {
        var id = id.toInt()
        val ref = database
            .child("User")
            .child(account_ref)
            .child("notifications")
        val ref2 = database
            .child("User")
            .child(account_ref)
            .child("notifications")
            .child("id_noti_is_readed")
        val noti1 = Notifications(
            to_person = "Huy44",
            "",
            day_create = "04/16/2022",
            from_person = "Cao Viet Huy",
            content = content,
            group = "Hoi Lap trinh Android",
            icon = icon,
            kind_of_noti = kind_of_noti,
            link_avatar_person = "https://firebasestorage.googleapis.com/v0/b/the-intership.appspot.com/o/avatar_user%2Fhuy1?alt=media&token=52d4b2e6-1a74-4ee5-ad19-51d9db2eead5",
            is_readed = false,
            link_post = "Link Post ne",
            id_comment = 22
        )
        noti1.set_day_and_time()
        noti1.set_day_create()
        CoroutineScope(Dispatchers.IO).launch {
            ref.child(id.toString()).setValue(noti1)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        println("debug add success")
                    } else {
                        println("debug add fail")
                    }
                }

            ref2.get().addOnSuccessListener {
                if (!it.exists()) {
                    ref2.setValue("0")
                }
            }
        }
    }


}