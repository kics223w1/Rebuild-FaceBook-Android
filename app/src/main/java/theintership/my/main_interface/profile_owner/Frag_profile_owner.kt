package theintership.my.main_interface.profile_owner

import android.graphics.Outline
import android.os.Bundle
import android.view.*
import android.widget.ImageView
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
import theintership.my.all_class.SharePrefValue
import theintership.my.databinding.FragProfileOwnerBinding
import theintership.my.main_interface.profile_owner.adapter.adapter_rcv_friends_in_profile
import theintership.my.main_interface.profile_owner.model.friend_in_profile
import theintership.my.main_interface.profile_owner.viewModel.ViewModelFragProfileOwner


class frag_profile_owner : Fragment() , adapter_rcv_friends_in_profile.Interaction {

    private var _binding: FragProfileOwnerBinding? = null
    private val binding get() = _binding!!
    private val database: DatabaseReference = Firebase.database.reference
    private lateinit var viewModelFragProfileOwner: ViewModelFragProfileOwner
    private lateinit var adapter_rcv_friend_in_profile : adapter_rcv_friends_in_profile
    private lateinit var mainInterfaceActivity: Main_Interface_Activity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragProfileOwnerBinding.inflate(inflater, container, false)
        set_boder_cover_image()
        viewModelFragProfileOwner =
            ViewModelProvider(this).get(ViewModelFragProfileOwner::class.java)
        mainInterfaceActivity = activity as Main_Interface_Activity
        adapter_rcv_friend_in_profile = adapter_rcv_friends_in_profile(this)
        get_list_friends_in_proflie()

        return binding.root
    }


    private fun get_list_friends_in_proflie() {
        val account_ref = SharePrefValue(mainInterfaceActivity).get_account_ref()
        val ref = database
            .child("User")
            .child(account_ref)
            .child("friends")
            .child("real")
        binding.fragProfileOwnerProgressRcvFriends.visibility = View.GONE
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val number_friends = snapshot.children.toMutableList().size
                val list = viewModelFragProfileOwner.setup_list_friend_in_profile(snapshot)
                //List just have maximum 6 elements
                if (list.size > 0){
                    binding.fragProfileOwnerLayoutFriends.visibility = View.VISIBLE
                    binding.fragProfileOwnerNumberFriends.text = "$number_friends friends"
                    val linearLayout: RecyclerView.LayoutManager = LinearLayoutManager(mainInterfaceActivity)
                    binding.fragProfileOwnerRcvFriends.layoutManager = linearLayout
                }
                adapter_rcv_friend_in_profile.submitList(list)
                binding.fragProfileOwnerRcvFriends.adapter = adapter_rcv_friend_in_profile
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        ref.addValueEventListener(postListener)
    }

    private fun set_boder_cover_image() {
        val curveRadius = 20F
        binding.coverImage.outlineProvider = object : ViewOutlineProvider() {
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
        binding.coverImage.clipToOutline = true
    }

    override fun onItemSelected(position: Int, item: friend_in_profile) {

    }

}
