package theintership.my.main_interface.profile

import android.content.Context
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
import theintership.my.all_class.MyMethod.Companion.replacefrag_in_main_interface_with_bundle
import theintership.my.all_class.MyMethod.Companion.set_up_image_by_glide
import theintership.my.all_class.SharePrefValue
import theintership.my.databinding.FragProfileOthersBinding
import theintership.my.main_interface.friends.fragment.frag_show_all_friends
import theintership.my.main_interface.profile.adapter.adapter_rcv_friends_in_profile
import theintership.my.main_interface.profile.dialog.dialog_remove_request_add_friend
import theintership.my.main_interface.profile.dialog.dialog_unfriend
import theintership.my.main_interface.profile.model.friend_in_profile
import theintership.my.main_interface.profile.viewModel.ViewModelFragProfile


class frag_profile_other : Fragment(), adapter_rcv_friends_in_profile.Interaction {

    private var _binding: FragProfileOthersBinding? = null
    private val binding get() = _binding!!
    private val database: DatabaseReference = Firebase.database.reference
    private lateinit var viewModelFragProfile: ViewModelFragProfile
    private lateinit var adapter_rcv_friend_in_profile: adapter_rcv_friends_in_profile
    private lateinit var mainInterfaceActivity: Main_Interface_Activity
    private var account_ref_from = ""
    private var account_ref_to = ""
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
        account_ref_from = arguments?.get("account ref from").toString()
        account_ref_to = arguments?.get("account ref to").toString()

        viewModelFragProfile =
            ViewModelProvider(this).get(ViewModelFragProfile::class.java)
        mainInterfaceActivity = activity as Main_Interface_Activity

        adapter_rcv_friend_in_profile =
            adapter_rcv_friends_in_profile(this, mainInterfaceActivity, account_ref_from)

        binding.fragProfileOthersBtnShowAllFriends.setOnClickListener {
            val b = Bundle()
            b.putString("account ref", account_ref_to)
            replacefrag_in_main_interface_with_bundle(
                "frag_show_all_friends",
                frag_show_all_friends(),
                mainInterfaceActivity.supportFragmentManager,
                b
            )
        }


        binding.fragProfileOthersBtnAddFriend.setOnClickListener {
            if (binding.fragProfileOthersBtnAddFriendText.text == "Friend") {
                show_dialog_unfriend(mainInterfaceActivity, account_ref_from, account_ref_to)
                return@setOnClickListener
            }
            if (binding.fragProfileOthersBtnAddFriendText.text == "Requesting") {
                show_dialog_remove_request_add_friend(
                    mainInterfaceActivity,
                    account_ref_from,
                    account_ref_to
                )
                return@setOnClickListener
            }
            binding.fragProfileOthersBtnAddFriendText.text = "Requesting"
            binding.fragProfileOthersBtnAddFriendIcon.visibility = View.INVISIBLE
            val account_ref_owner = SharePrefValue(mainInterfaceActivity).get_account_ref()
            val link_avatar_owner = SharePrefValue(mainInterfaceActivity).get_link_avatar()
            val user_name_owner = SharePrefValue(mainInterfaceActivity).get_user_name()
            viewModelFragProfile.add_friend(
                from = account_ref_owner,
                to = account_ref_to,
                user_name = user_name_owner,
                link_avatar = link_avatar_owner
            )
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
        CoroutineScope(Dispatchers.IO).launch {
            is_pending_friend()
            is_friend()
            get_list_friends_in_proflie()
            set_name()
            set_avatar_and_cover_image()
        }
    }

    private fun set_avatar_and_cover_image() {
        val ref = database.child("User")
            .child(account_ref_to)
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


    private fun is_pending_friend() {
        val ref = database.child("User")
            .child(account_ref_from)
            .child("friends")
            .child("request")
            .child(account_ref_to)
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                binding.fragProfileOthersBtnAddFriendText.text = "Requesting"
                binding.fragProfileOthersBtnAddFriendIcon.visibility = View.INVISIBLE
            }
        }
    }

    fun show_dialog_remove_request_add_friend(context: Context, from: String, to: String) {
        val dialog = dialog_remove_request_add_friend(context)
        dialog.show()
        dialog.btn_remove_request.setOnClickListener {
            binding.fragProfileOthersBtnAddFriendText.text = "Add friend"
            binding.fragProfileOthersBtnAddFriendIcon.visibility = View.VISIBLE
            viewModelFragProfile.remove_request_add_friend(context, from, to)
            dialog.dismiss()
        }
    }


    fun show_dialog_unfriend(context: Context, from: String, to: String) {
        val dialog = dialog_unfriend(context)
        dialog.show()
        dialog.btn_unfriend.setOnClickListener {
            binding.fragProfileOthersBtnAddFriendText.text = "Add friend"
            binding.fragProfileOthersBtnAddFriendIcon.visibility = View.VISIBLE
            viewModelFragProfile.unfriend(context, from, to)
            dialog.dismiss()
        }
    }

    private fun is_friend() {
        val ref = database.child("User")
            .child(account_ref_from)
            .child("friends")
            .child("real")
            .child(account_ref_to)
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                binding.fragProfileOthersBtnAddFriendText.text = "Friend"
                binding.fragProfileOthersBtnAddFriendIcon.visibility = View.INVISIBLE
            }
        }
    }

    private fun set_name() {
        val ref = database.child("User")
            .child(account_ref_to)
            .child("user info")
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                val name = it.child("fullname").getValue().toString()
                println("debug name other: $name")
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
            .child(account_ref_to)
            .child("friends")
            .child("real")
        ref.get().addOnSuccessListener {
            if (!it.exists()){
                binding.fragProfileOthersProgressRcvFriends.visibility = View.GONE
                binding.fragProfileOthersNumberFriends.text = "0 friends"
                binding.fragProfileOthersBtnShowAllFriends.visibility = View.GONE
            }
        }
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val number_friends = snapshot.children.toMutableList().size
                val list = viewModelFragProfile.setup_list_friend_in_profile(snapshot)
                //List just have maximum 6 elements
                if (list.size > 0) {
                    binding.fragProfileOthersProgressRcvFriends.visibility = View.GONE
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
