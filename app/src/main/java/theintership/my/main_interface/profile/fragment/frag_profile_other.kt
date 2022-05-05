package theintership.my.main_interface.profile

import android.graphics.Outline
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import theintership.my.Main_Interface_Activity
import theintership.my.all_class.MyMethod.Companion.not_implement
import theintership.my.all_class.MyMethod.Companion.set_up_image_by_glide
import theintership.my.all_class.SharePrefValue
import theintership.my.databinding.FragProfileOthersBinding
import theintership.my.main_interface.profile.adapter.adapter_rcv_friends_in_profile
import theintership.my.main_interface.profile.model.friend_in_profile
import theintership.my.main_interface.profile.viewModel.ViewModelFragProfile


class frag_profile_other : Fragment(), adapter_rcv_friends_in_profile.Interaction {

    private var _binding: FragProfileOthersBinding? = null
    private val binding get() = _binding!!
    private val database: DatabaseReference = Firebase.database.reference
    private lateinit var viewModelFragProfile: ViewModelFragProfile
    private lateinit var adapter_rcv_friend_in_profile: adapter_rcv_friends_in_profile
    private lateinit var mainInterfaceActivity: Main_Interface_Activity
    private var account_ref_other = ""
    private var done_name = false
    private var done_avatar_and_cover_image = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragProfileOthersBinding.inflate(inflater, container, false)
        set_boder_cover_image()
        setUpProfile()
        account_ref_other = arguments?.get("account ref").toString()


        binding.fragProfileOthersBtnAddFriend.setOnClickListener {
            if (binding.fragProfileOthersBtnAddFriend.text == "Friend"){
                //Dialog delete friend is show
                return@setOnClickListener
            }
            binding.fragProfileOthersBtnAddFriend.text = "Requesting"
            val account_ref_owner = SharePrefValue(mainInterfaceActivity).get_account_ref()
            val link_avatar_other = SharePrefValue(mainInterfaceActivity).get_link_avatar()
            val user_name_other = SharePrefValue(mainInterfaceActivity).get_user_name_other()
            viewModelFragProfile.add_friend(
                from = account_ref_owner,
                to = account_ref_other,
                user_name = user_name_other,
                link_avatar =  link_avatar_other)
        }

        binding.btnMore.setOnClickListener {
            not_implement(mainInterfaceActivity)
        }

        binding.btnDoneSetAvatarBack.setOnClickListener {
            mainInterfaceActivity.supportFragmentManager.popBackStack()
        }

        return binding.root
    }


    private fun setUpProfile() {
        viewModelFragProfile =
            ViewModelProvider(this).get(ViewModelFragProfile::class.java)
        mainInterfaceActivity = activity as Main_Interface_Activity
        adapter_rcv_friend_in_profile = adapter_rcv_friends_in_profile(this, mainInterfaceActivity)

        CoroutineScope(Dispatchers.IO).launch {
            is_friend()
            get_list_friends_in_proflie()
            set_name()
            set_avatar_and_cover_image()
        }
    }

    private fun set_avatar_and_cover_image() {
        val ref = database.child("User")
            .child(account_ref_other)
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                val link_avatar = it.child("link avatar").getValue().toString()
                val link_cover_image = it.child("link cover image").getValue().toString()

                SharePrefValue(mainInterfaceActivity).store_string(link_avatar, "link avatar other")
                SharePrefValue(mainInterfaceActivity).store_string(
                    link_cover_image,
                    "link cover image other"
                )

                set_up_image_by_glide(
                    link_avatar,
                    binding.fragProfileOthersAvatar,
                    mainInterfaceActivity
                )
                set_up_image_by_glide(
                    link_cover_image,
                    binding.fragProfileOthersCoverImage,
                    mainInterfaceActivity
                )
                done_avatar_and_cover_image = true
                if (done_name && done_avatar_and_cover_image) {
                    binding.fragProfileOthersProgressAddFriend.visibility = View.GONE
                    binding.fragProfileOthersBtnAddFriend.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun is_friend(){
        val account_ref_owner = SharePrefValue(mainInterfaceActivity).get_account_ref()
        val ref = database.child("User")
            .child(account_ref_owner)
            .child("friends")
            .child("real")
            .child(account_ref_other)
        ref.get().addOnSuccessListener {
            if (it.exists()){
                binding.fragProfileOthersBtnAddFriend.text = "Friend"
            }
        }
    }

    private fun set_name() {
        val ref = database.child("User")
            .child(account_ref_other)
            .child("user info")
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                val name = it.child("fullname").getValue().toString()

                SharePrefValue(mainInterfaceActivity).store_string(name, "user name other")
                binding.fragProfileOthersName.text = name

                done_name = true
                if (done_name && done_avatar_and_cover_image) {
                    binding.fragProfileOthersProgressAddFriend.visibility = View.GONE
                    binding.fragProfileOthersBtnAddFriend.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun get_list_friends_in_proflie() {
        val ref = database
            .child("User")
            .child(account_ref_other)
            .child("friends")
            .child("real")
        binding.fragProfileOthersProgressRcvFriends.visibility = View.GONE
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val number_friends = snapshot.children.toMutableList().size
                val list = viewModelFragProfile.setup_list_friend_in_profile(snapshot)
                //List just have maximum 6 elements
                if (list.size > 0) {
                    binding.fragProfileOthersLayoutFriends.visibility = View.VISIBLE
                    binding.fragProfileOthersNumberFriends.text = "$number_friends friends"
                    val linearLayout: RecyclerView.LayoutManager =
                        LinearLayoutManager(mainInterfaceActivity)
                    binding.fragProfileOthersRcvFriends.layoutManager = linearLayout
                }
                adapter_rcv_friend_in_profile.submitList(list)
                binding.fragProfileOthersRcvFriends.adapter = adapter_rcv_friend_in_profile
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        ref.addValueEventListener(postListener)
    }

    private fun set_boder_cover_image() {
        val curveRadius = 20F
        binding.fragProfileOthersCoverImage.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                outline?.setRoundRect(
                    0,
                    0,
                    view!!.width,
                    (view.height + curveRadius).toInt(),
                    curveRadius
                )
            }
        }
        binding.fragProfileOthersCoverImage.clipToOutline = true
    }


    override fun onItemSelected(position: Int, item: friend_in_profile) {

    }

}
