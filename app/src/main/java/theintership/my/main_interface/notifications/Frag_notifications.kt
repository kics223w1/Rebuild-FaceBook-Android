package theintership.my.main_interface.notifications

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.Main_Interface_Activity
import theintership.my.R
import theintership.my.all_class.MyMethod.Companion.count_days
import theintership.my.all_class.MyMethod.Companion.set_today
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.main_interface.notifications.adapter.*
import theintership.my.main_interface.notifications.model.Notifications
import theintership.my.main_interface.notifications.viewModel.ViewModelFragNotifications


class frag_notifications : Fragment(), adapter_rcv_earlier.Interaction,
    adapter_rcv_new.Interaction{

    private lateinit var database: DatabaseReference
    private lateinit var viewModelFragNotifications: ViewModelFragNotifications
    private lateinit var mainInterfaceActivity: Main_Interface_Activity
    private lateinit var adapterRcvNew: adapter_rcv_new
    private lateinit var adapterRcvEarlier: adapter_rcv_earlier
    private lateinit var rcv_new: RecyclerView
    private lateinit var rcv_earlier: RecyclerView
    private lateinit var progress_loading_rcv_new: ProgressBar
    private lateinit var progress_loading_rcv_earlier: ProgressBar
    private lateinit var layout : SwipeRefreshLayout
    private lateinit var account_ref : String
    private val new_notis: MutableList<Notifications> = mutableListOf()
    private val earlier_notis: MutableList<Notifications> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_notifications, container, false)
        mainInterfaceActivity = activity as Main_Interface_Activity
        layout = view.findViewById(R.id.frag_notificaions_swipelayout)
        rcv_new = view.findViewById(R.id.frag_notifications_rcv_new)
        progress_loading_rcv_new =
            view.findViewById(R.id.frag_notifications_progressBar_rcv_new)
        progress_loading_rcv_earlier =
            view.findViewById(R.id.frag_notifications_progressBar_rcv_earlier)
        rcv_earlier = view.findViewById(R.id.frag_notifications_rcv_earlier)
        val linearLayout: RecyclerView.LayoutManager = LinearLayoutManager(mainInterfaceActivity)
        val linearLayout1: RecyclerView.LayoutManager = LinearLayoutManager(mainInterfaceActivity)
        val sharedPref = mainInterfaceActivity.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        account_ref = sharedPref.getString("account_ref" , "").toString()
        database = Firebase.database.reference

        adapterRcvNew = adapter_rcv_new(this)
        adapterRcvEarlier = adapter_rcv_earlier(this)
        viewModelFragNotifications =
            ViewModelProvider(this).get(ViewModelFragNotifications::class.java)
        rcv_new.layoutManager = linearLayout
        rcv_earlier.layoutManager = linearLayout1

        rcv_new.isNestedScrollingEnabled = false
        rcv_earlier.isNestedScrollingEnabled = false

        listen_new_noti_and_add_it_into_list(false)

        layout.setOnRefreshListener {
            new_notis.clear()
            earlier_notis.clear()
            rcv_new.visibility = View.GONE
            rcv_earlier.visibility = View.GONE
            progress_loading_rcv_new.visibility = View.VISIBLE
            progress_loading_rcv_earlier.visibility = View.VISIBLE
            listen_new_noti_and_add_it_into_list(true)
        }



        return view
    }

    override fun onItemSelected(position: Int, item: Notifications) {
        val s = position.toString()
        s.showToastShort(mainInterfaceActivity)
    }

    fun listen_new_noti_and_add_it_into_list(layout_refresh : Boolean) {
        val ref_noti = database
            .child("User")
            .child(account_ref)
            .child("notifications")
        ref_noti.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val noti = viewModelFragNotifications.setup_noti(snapshot)
                val today = set_today()
                progress_loading_rcv_earlier.visibility = View.GONE
                progress_loading_rcv_new.visibility = View.GONE
                rcv_new.visibility = View.VISIBLE
                rcv_earlier.visibility = View.VISIBLE
                if (count_days(noti.day_create.toString(), today) < 14) {
                    if (noti.day_create == today) {
                        new_notis.add(0 , noti)
                        adapterRcvNew.submitList(new_notis)
                        rcv_new.adapter = adapterRcvNew
                    } else {
                        earlier_notis.add(0 ,noti)
                        adapterRcvEarlier.submitList(earlier_notis)
                        rcv_earlier.adapter = adapterRcvEarlier
                    }
                }
                if (layout_refresh){
                    layout.isRefreshing = false
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                println("debug $snapshot")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}

