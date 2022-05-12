package theintership.my.main_interface.adapter

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.R
import theintership.my.main_interface.fragment.*
import theintership.my.main_interface.friends.frag_friends
import theintership.my.main_interface.message.frag_message
import theintership.my.main_interface.notifications.frag_notifications
import theintership.my.main_interface.profile.frag_profile_owner

class TabPageAdapter(activity: FragmentActivity, private val tab_count: Int) :
    FragmentStateAdapter(activity) {
    private val context = activity
    override fun getItemCount(): Int = tab_count

    private lateinit var tv_number_noti: TextView
    private lateinit var layout_tv_number_noti: CardView
    private val database = Firebase.database.reference
    private val channel_id = "Channel Notifications"
    var id_noti_is_readed = 0

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> frag_home()
            1 -> frag_friends()
            2 -> frag_message()
            3 -> frag_profile_owner()
            4 -> frag_notifications()
            5 -> frag_setting()
            else -> frag_home()
        }
    }

    fun set_custom_icon_notificaion(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_icon_notification, null)
        tv_number_noti = view.findViewById<TextView>(R.id.tv_custom_icon_notification)
        layout_tv_number_noti =
            view.findViewById(R.id.layout_tv_custom_icon_notifications)
        set_tv_noti()
        return view
    }

    fun remove_layout_number_noti() {
        layout_tv_number_noti.visibility = View.GONE
        tv_number_noti.visibility = View.GONE
    }

    private fun set_tv_noti() {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val account_ref = sharedPref?.getString("account_ref", "").toString()
        val ref_id_noti_is_readed = database
            .child("User")
            .child(account_ref)
            .child("notifications")
            .child("id_noti_is_readed")
        ref_id_noti_is_readed.get().addOnSuccessListener {
            if (it.exists()) {
                val mId = it.getValue().toString()
                id_noti_is_readed = mId.toInt()
                get_list_noti(account_ref)
            }
        }
    }

    private fun get_list_noti(account_ref: String) {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val ref_notifications = database
            .child("User")
            .child(account_ref)
            .child("notifications")
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.toMutableList()
                val id_noti_is_readed2 = sharedPref.getInt("id_noti_is_readed", 0)
                if (id_noti_is_readed2 > id_noti_is_readed) {
                    id_noti_is_readed = id_noti_is_readed2
                }
                val number_noti_not_read = list.size - id_noti_is_readed
                createNotificationsChannel()
                var builder: NotificationCompat.Builder =
                    NotificationCompat.Builder(context, channel_id)
                        .setContentTitle("The intership notifications")
                        .setSmallIcon(R.drawable.icon_facebook)
                        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                if (number_noti_not_read > 0 && number_noti_not_read <= 99) {
                    layout_tv_number_noti.visibility = View.VISIBLE
                    tv_number_noti.visibility = View.VISIBLE
                    tv_number_noti.text = number_noti_not_read.toString()
                    builder.setContentText("You have $number_noti_not_read new notifications")
                    with(NotificationManagerCompat.from(context)){
                        notify(0 , builder.build())
                    }
                }
                if (number_noti_not_read > 99) {
                    layout_tv_number_noti.visibility = View.VISIBLE
                    tv_number_noti.visibility = View.VISIBLE
                    tv_number_noti.text = "99"
                    builder.setContentText("You have 99 new notifications")
                    with(NotificationManagerCompat.from(context)){
                        notify(0 , builder.build())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        ref_notifications.addValueEventListener(postListener)
    }


    private fun createNotificationsChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "App Notifications"
            val description_text = "Description Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channel_id, name, importance).apply {
                description = description_text
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }
}