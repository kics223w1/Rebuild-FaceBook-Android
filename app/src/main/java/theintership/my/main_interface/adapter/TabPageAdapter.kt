package theintership.my.main_interface.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.R
import theintership.my.main_interface.fragment.*
import theintership.my.main_interface.notifications.frag_notifications

class TabPageAdapter(activity: FragmentActivity, private val tab_count: Int) :
    FragmentStateAdapter(activity) {
    private val context = activity
    override fun getItemCount(): Int = tab_count

    private lateinit var tv_number_noti: TextView
    private lateinit var layout_tv_number_noti: CardView

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> frag_home()
            1 -> frag_group()
            2 -> frag_video()
            3 -> frag_heart()
            4 -> frag_notifications()
            5 -> frag_setting()
            else -> frag_home()
        }
    }

    fun set_custom_icon_notificaion(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_icon_notification, null)
        tv_number_noti = view.findViewById<TextView>(R.id.tv_custom_icon_notification)
        layout_tv_number_noti =
            view.findViewById<CardView>(R.id.layout_tv_custom_icon_notifications)

        return view
    }

    private fun set_tv_noti() {
        val database = Firebase.database.reference
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val account_ref = sharedPref?.getString("account_ref", "").toString()
        val ref_id_noti_is_readed = database
            .child("User")
            .child(account_ref)
            .child("notifications")
            .child("id_noti_is_readed")
        ref_id_noti_is_readed.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {

            }
        }
    }
}