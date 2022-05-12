package theintership.my.main_interface.friends

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.activity.Main_Interface_Activity
import theintership.my.R
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.databinding.FragFriendsBinding
import theintership.my.main_interface.friends.adapter.adapter_rcv_friends_may_know
import theintership.my.main_interface.friends.adapter.adapter_rcv_friends_request
import theintership.my.main_interface.friends.model.Friends
import theintership.my.main_interface.friends.viewmodel.ViewModelFragFriends


class frag_friends : Fragment(), adapter_rcv_friends_request.Interaction,
    adapter_rcv_friends_may_know.Interaction2 {

    private var _binding: FragFriendsBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private var list_fr_request: MutableList<Friends> = mutableListOf()
    private var list_fr_may_know: MutableList<Friends> = mutableListOf()
    private lateinit var viewModelFragFriends: ViewModelFragFriends
    private lateinit var adapter_fr_request: adapter_rcv_friends_request
    private lateinit var adapter_fr_may_know: adapter_rcv_friends_may_know
    private lateinit var mainInterfaceActivity: Main_Interface_Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragFriendsBinding.inflate(inflater, container, false)
        database = Firebase.database.reference
        viewModelFragFriends = ViewModelProvider(this).get(ViewModelFragFriends::class.java)
        mainInterfaceActivity = activity as Main_Interface_Activity

        val sharedPref = mainInterfaceActivity.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val account_ref = sharedPref.getString("account ref", "null").toString()

        adapter_fr_request = adapter_rcv_friends_request(this, mainInterfaceActivity)
        adapter_fr_may_know = adapter_rcv_friends_may_know(this)

        setupRCV()
        listen_friends_request(account_ref, false)
        getlist_friends_may_know(account_ref)

        binding.layoutFragFriends.setOnRefreshListener {
            list_fr_request.clear()
            binding.fragFirendsRcvRequest.visibility = View.GONE
            binding.fragFriendsProgressBarRcvRequest.visibility = View.VISIBLE
            listen_friends_request(account_ref, true)
        }


        return binding.root
    }

    private fun setupRCV() {
        val linearLayout: RecyclerView.LayoutManager = LinearLayoutManager(mainInterfaceActivity)
        val linearLayout1: RecyclerView.LayoutManager = LinearLayoutManager(mainInterfaceActivity)

        binding.fragFirendsRcvRequest.layoutManager = linearLayout
        binding.fragFriednsRcvMayKnow.layoutManager = linearLayout1

        binding.fragFirendsRcvRequest.isNestedScrollingEnabled = false
        binding.fragFriednsRcvMayKnow.isNestedScrollingEnabled = false
    }

    private fun getlist_friends_may_know(account_ref: String) {
        val ref_fr = database.child("User")
            .child(account_ref)
            .child("friends")
            .child("may know")
        binding.fragFirendsProgressBarRcvMayKnow.visibility = View.GONE

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.toMutableList()
                for (ele in list) {
                    val fr = viewModelFragFriends.setup_friends(ele)
                    list_fr_may_know.add(fr)
                    adapter_fr_may_know.submitList(list_fr_may_know)
                    binding.fragFriednsRcvMayKnow.visibility = View.VISIBLE
                    binding.fragFriednsRcvMayKnow.adapter = adapter_fr_may_know
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        ref_fr.addValueEventListener(postListener)
    }


    private fun listen_friends_request(account_ref: String, layout_refresh: Boolean) {
        val ref_fr = database.child("User")
            .child(account_ref)
            .child("friends")
            .child("request")

        binding.fragFriendsProgressBarRcvRequest.visibility = View.GONE

        ref_fr.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val fr = viewModelFragFriends.setup_friends(snapshot)
                list_fr_request.add(0, fr)
                adapter_fr_request.submitList(list_fr_request)
                binding.fragFirendsRcvRequest.visibility = View.VISIBLE
                binding.fragFirendsRcvRequest.adapter = adapter_fr_request
                if (layout_refresh) {
                    binding.layoutFragFriends.isRefreshing = false
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onItemSelectedFrRequest(position: Int, item: Friends) {
        //Open user profile
    }

    override fun onItemSelectedFrMayKnow(position: Int, item: Friends) {
        val s = "Not Implement"
        s.showToastShort(mainInterfaceActivity)
    }


}