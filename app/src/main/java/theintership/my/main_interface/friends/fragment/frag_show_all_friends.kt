package theintership.my.main_interface.friends.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import theintership.my.activity.Main_Interface_Activity
import theintership.my.R
import theintership.my.all_class.SharePrefValue
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
    private lateinit var adapter: adapter_rcv_show_all_friends
    private val database: DatabaseReference = Firebase.database.reference
    private lateinit var account_ref: String
    private var friends_all = mutableListOf<friend_in_show_all>()
    private var friends_mutual = mutableListOf<friend_in_show_all>()

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
        get_friends_all(account_ref)

        binding.fragShowAllFriendsBtnAllFr.setOnClickListener {
            binding.fragShowAllFriendsRcv.visibility = View.INVISIBLE
            binding.fragShowAllFriendsProgress.visibility = View.VISIBLE

            get_friends_all(account_ref)
        }

        binding.fragShowAllFriendsBtnMutualFr.setOnClickListener {
            binding.fragShowAllFriendsRcv.visibility = View.INVISIBLE
            binding.fragShowAllFriendsProgress.visibility = View.VISIBLE

            val account_ref_owner = SharePrefValue(mainInterfaceActivity).get_account_ref()
            get_friends_mutual(account_ref_owner)
        }


        return binding.root
    }

    private fun get_friends_mutual(account_ref_owner: String) {
        val ref = database.child("User")
            .child(account_ref_owner)
            .child("friends")
            .child("real")
        ref.get().addOnSuccessListener {
            if (!it.exists()){
                binding.fragShowAllFriendsProgress.visibility = View.GONE
            }
        }
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = viewModelFragProfileOwner.setup_friend_in_show_all(snapshot)
                for (i in 0 until friends_all.size) {
                    if (list.contains(friends_all[i])) {
                        friends_mutual.add(friends_all[i])
                    }
                }
                binding.fragShowAllFriendsProgress.visibility = View.GONE
                binding.fragShowAllFriendsNumberFriend.text = "${friends_mutual.size} mutual friends"
                binding.fragShowAllFriendsRcv.visibility = View.VISIBLE

                adapter.submitList(friends_mutual)
                binding.fragShowAllFriendsRcv.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        ref.addValueEventListener(postListener)
    }


    private fun get_friends_all(account_ref: String) {
        val ref = database.child("User")
            .child(account_ref)
            .child("friends")
            .child("real")
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                friends_all = viewModelFragProfileOwner.setup_friend_in_show_all(snapshot)
                adapter.submitList(friends_all)

                binding.fragShowAllFriendsProgress.visibility = View.GONE
                binding.fragShowAllFriendsNumberFriend.visibility = View.VISIBLE
                binding.fragShowAllFriendsRcv.visibility = View.VISIBLE

                binding.fragShowAllFriendsNumberFriend.text = "${friends_all.size} friends"
                binding.fragShowAllFriendsRcv.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        ref.addValueEventListener(postListener)
    }


    override fun onItemSelected(position: Int, item: friend_in_show_all) {

    }

}