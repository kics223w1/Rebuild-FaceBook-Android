package theintership.my.main_interface.notifications

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.Main_Interface_Activity
import theintership.my.R
import theintership.my.all_class.Dowload_image_from_Firebase_by_dowloadURL
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.main_interface.notifications.adapter.adapter_rcv_earlier
import theintership.my.main_interface.notifications.adapter.adapter_rcv_new
import theintership.my.main_interface.notifications.model.Notifications
import theintership.my.main_interface.notifications.viewModel.ViewModelFragNotifications


class frag_notifications : Fragment(), adapter_rcv_earlier.Interaction,
    adapter_rcv_new.Interaction {

    private lateinit var database: DatabaseReference
    private lateinit var viewModelFragNotifications: ViewModelFragNotifications
    private lateinit var mainInterfaceActivity: Main_Interface_Activity
    private lateinit var adapterRcvNew: adapter_rcv_new
    private lateinit var adapterRcvEarlier: adapter_rcv_earlier

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_notifications, container, false)
        mainInterfaceActivity = activity as Main_Interface_Activity
        val layout = view.findViewById<SwipeRefreshLayout>(R.id.frag_notificaions_swipelayout)
        val rcv_new = view.findViewById<RecyclerView>(R.id.frag_notifications_rcv_new)
        val progress_loading_rcv_new =
            view.findViewById<ProgressBar>(R.id.frag_notifications_progressBar_rcv_new)
        val progress_loading_rcv_earlier =
            view.findViewById<ProgressBar>(R.id.frag_notifications_progressBar_rcv_earlier)
        val rcv_earlier = view.findViewById<RecyclerView>(R.id.frag_notifications_rcv_earlier)
        val linearLayout: RecyclerView.LayoutManager = LinearLayoutManager(mainInterfaceActivity)
        val linearLayout1: RecyclerView.LayoutManager = LinearLayoutManager(mainInterfaceActivity)
        val btn = view.findViewById<TextView>(R.id.frag_notifications_tv_new)

        database = Firebase.database.reference

        val ref = database
            .child("User")
            .child("admin1")
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
        btn.setOnClickListener {
            for (i in 0 until 40) {
                noti1.set_day_and_time()
                if (i % 2 == 0) {
                    noti1.content =
                        "Cao Viet Huy da tra loi binh luan cua ban trong Hoi Lap trinh Android cua ban trong hoi lap trinh IOS nha"
                    noti1.icon = "love"
                    noti1.is_readed = true
                } else {
                    noti1.is_readed = false
                    noti1.icon = "love love"
                }
                ref.child("${i}").setValue(noti1)
            }
        }


        adapterRcvNew = adapter_rcv_new(this)
        adapterRcvEarlier = adapter_rcv_earlier(this)
        viewModelFragNotifications =
            ViewModelProvider(this).get(ViewModelFragNotifications::class.java)
        rcv_new.layoutManager = linearLayout
        rcv_earlier.layoutManager = linearLayout1

        rcv_new.isNestedScrollingEnabled = false
        rcv_earlier.isNestedScrollingEnabled = false

        viewModelFragNotifications.setup_list_new_noti_and_old_noti("admin1")

        viewModelFragNotifications.getList_NewNoti_LiveData()
            .observe(viewLifecycleOwner, Observer {
                adapterRcvNew.submitList(it)
                println("debug obser sz new: ${it.size}")
                progress_loading_rcv_new.visibility = View.GONE
                rcv_new.visibility = View.VISIBLE
                rcv_new.adapter = adapterRcvNew
            })

        viewModelFragNotifications.getList_OldNoti_LiveData().observe(
            viewLifecycleOwner, Observer {
                println("debug vao obser old ne ${it.size}")
                adapterRcvEarlier.submitList(it)
                progress_loading_rcv_earlier.visibility = View.GONE
                rcv_earlier.visibility = View.VISIBLE
                rcv_earlier.adapter = adapterRcvEarlier
            }
        )

        var id = 40
        layout.setOnRefreshListener {
            noti1.set_day_and_time()
            println("debug vao layout refresh ne")
            ref.child(id.toString()).setValue(noti1)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        layout.isRefreshing = false
                    }else{
                        layout.isRefreshing = false
                    }
                }
            id++
        }


        return view
    }

    override fun onItemSelected(position: Int, item: Notifications) {
        val s = position.toString()
        s.showToastShort(mainInterfaceActivity)
    }

}

