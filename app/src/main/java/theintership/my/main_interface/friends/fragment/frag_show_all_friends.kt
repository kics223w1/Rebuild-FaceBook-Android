package theintership.my.main_interface.friends.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.Main_Interface_Activity
import theintership.my.R
import theintership.my.databinding.FragShowAllFriendsBinding
import theintership.my.main_interface.friends.adapter.adapter_rcv_show_all_friends
import theintership.my.main_interface.friends.model.friend_in_show_all
import theintership.my.main_interface.profile.viewModel.ViewModelFragProfile


class frag_show_all_friends : Fragment(R.layout.frag_show_all_friends),
    adapter_rcv_show_all_friends.Interaction {

    private var _binding: FragShowAllFriendsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainInterfaceActivity: Main_Interface_Activity
    private lateinit var viewModelFragProfileOwner: ViewModelFragProfile
    private lateinit var adapter : adapter_rcv_show_all_friends
    private val database: DatabaseReference = Firebase.database.reference
    private lateinit var account_ref: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragShowAllFriendsBinding.inflate(inflater, container, false)
        mainInterfaceActivity = activity as Main_Interface_Activity
        viewModelFragProfileOwner = ViewModelProvider(this).get(ViewModelFragProfile::class.java)

        adapter = adapter_rcv_show_all_friends(this, mainInterfaceActivity)
        val linearLayout: RecyclerView.LayoutManager = LinearLayoutManager(mainInterfaceActivity)
        binding.fragShowAllFriendsRcv.layoutManager = linearLayout
        account_ref = arguments?.get("account ref").toString()
        get_listfr(account_ref)




        return binding.root
    }

    private fun get_listfr(account_ref : String) {
        val ref =  database.child("User")
            .child(account_ref)
            .child("friends")
            .child("real")
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = viewModelFragProfileOwner.setup_friend_in_show_all(snapshot)
                adapter.submitList(list)

                binding.fragShowAllFriendsProgress.visibility = View.GONE
                binding.fragShowAllFriendsNumberFriend.visibility = View.VISIBLE
                binding.fragShowAllFriendsRcv.visibility = View.VISIBLE

                binding.fragShowAllFriendsNumberFriend.text = "${list.size} friends"
                binding.fragShowAllFriendsRcv.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        ref.addValueEventListener(postListener)
    }


    override fun onItemSelected(position: Int, item: friend_in_show_all) {

    }

}