package theintership.my.main_interface.profile

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Outline
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.Main_Interface_Activity
import theintership.my.R
import theintership.my.all_class.MyMethod
import theintership.my.all_class.MyMethod.Companion.not_implement
import theintership.my.all_class.MyMethod.Companion.replacefrag_in_main_interface_with_slide_in_left
import theintership.my.all_class.MyMethod.Companion.set_up_image_by_glide
import theintership.my.all_class.MyMethod.Companion.showToastLong
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.all_class.SharePrefValue
import theintership.my.databinding.FragProfileOwnerBinding
import theintership.my.main_interface.friends.fragment.frag_show_all_friends
import theintership.my.main_interface.profile.adapter.adapter_rcv_friends_in_profile
import theintership.my.main_interface.profile.dialog.dialog_set_avatar_and_coverImage
import theintership.my.main_interface.profile.model.friend_in_profile
import theintership.my.main_interface.profile.viewModel.ViewModelFragProfile
import theintership.my.signin_signup.fragment.frag_show_image_for_chosing_avatar


class frag_profile_owner : Fragment(), adapter_rcv_friends_in_profile.Interaction {

    private var _binding: FragProfileOwnerBinding? = null
    private val binding get() = _binding!!
    private val database: DatabaseReference = Firebase.database.reference
    private lateinit var viewModelFragProfileOwner: ViewModelFragProfile
    private lateinit var adapter_rcv_friend_in_profile: adapter_rcv_friends_in_profile
    private lateinit var mainInterfaceActivity: Main_Interface_Activity
    private val REQUEST_CODE = 1
    private val REQUEST_IMAGE_CAPTURE_AVATAR = 2
    private val REQUEST_IMAGE_CAPTURE_COVER_IMAGE = 3


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragProfileOwnerBinding.inflate(inflater, container, false)
        set_boder_cover_image()
        setUpProfile()


        binding.cameraAvatar.setOnClickListener {
            setAvatar_CoverImage(requestCode = REQUEST_IMAGE_CAPTURE_AVATAR)
        }

        binding.cameraCoverImage.setOnClickListener {
            setAvatar_CoverImage(requestCode = REQUEST_IMAGE_CAPTURE_COVER_IMAGE)
        }

        binding.btnSetStatus.setOnClickListener {
            not_implement(mainInterfaceActivity)
        }

        binding.btnUpdateInformation.setOnClickListener {
            not_implement(mainInterfaceActivity)
        }

        binding.btnMore.setOnClickListener {
            not_implement(mainInterfaceActivity)
        }

        binding.fragProfileOwnerBtnShowAllFriends.setOnClickListener {
            val b = Bundle()
            val account_ref = SharePrefValue(mainInterfaceActivity).get_account_ref()
            b.putString("account ref", account_ref)
            MyMethod.replacefrag_in_main_interface_with_bundle(
                "frag_show_all_friends",
                frag_show_all_friends(),
                mainInterfaceActivity.supportFragmentManager,
                b
            )
        }

        return binding.root
    }


    private fun setUpProfile() {
        viewModelFragProfileOwner =
            ViewModelProvider(this).get(ViewModelFragProfile::class.java)
        mainInterfaceActivity = activity as Main_Interface_Activity

        val account_ref_owner = SharePrefValue(mainInterfaceActivity).get_account_ref()
        adapter_rcv_friend_in_profile = adapter_rcv_friends_in_profile(
            this,
            mainInterfaceActivity,
            account_ref_from = account_ref_owner
        )
        get_list_friends_in_proflie()

        val name = SharePrefValue(mainInterfaceActivity).get_user_name()
        binding.fragProfileOwnerName.text = name

        val link_avatar = SharePrefValue(mainInterfaceActivity).get_link_avatar()
        set_up_image_by_glide(link_avatar, binding.fragProfileOwnerAvatar, mainInterfaceActivity)

        val ref = database.child("User")
            .child(account_ref_owner)
            .child("link cover image")
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                set_up_image_by_glide(
                    it.getValue().toString(),
                    binding.fragProfileOwnerCoverImage,
                    mainInterfaceActivity
                )
            }
        }
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
                if (list.size > 0) {
                    binding.fragProfileOwnerLayoutFriends.visibility = View.VISIBLE
                    binding.fragProfileOwnerNumberFriends.text = "$number_friends friends"
                    val linearLayout: RecyclerView.LayoutManager =
                        LinearLayoutManager(mainInterfaceActivity)
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
        binding.fragProfileOwnerCoverImage.outlineProvider = object : ViewOutlineProvider() {
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
        binding.fragProfileOwnerCoverImage.clipToOutline = true
    }

    private fun setAvatar_CoverImage(requestCode: Int) {
        val dialog = dialog_set_avatar_and_coverImage(mainInterfaceActivity)
        dialog.show()
        dialog.btn_take_picture.setOnClickListener {
            dispatchTakePictureIntent(requestCode)
            dialog.dismiss()
        }
    }


    private fun dispatchTakePictureIntent(requestCode: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, requestCode)
        } catch (e: ActivityNotFoundException) {
            println("debug e in dispatchTakePictureIntent: $e")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE_COVER_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val account_ref = SharePrefValue(mainInterfaceActivity).get_account_ref()
            binding.fragProfileOwnerCoverImage.setImageBitmap(imageBitmap)
            viewModelFragProfileOwner.setup_link_coverImage_and_storage(
                account_ref,
                imageBitmap
            )
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE_AVATAR && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val account_ref = SharePrefValue(mainInterfaceActivity).get_account_ref()
            binding.fragProfileOwnerAvatar.setImageBitmap(imageBitmap)
            viewModelFragProfileOwner.setup_link_avatar_and_storage(account_ref, imageBitmap)
        }
    }

    private fun check_permission_read_image() {
        when {
            ContextCompat.checkSelfPermission(
                mainInterfaceActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                move_to_frag_show_image()
            }
            else -> {
                //If you use signup1activty.requestPermission
                //OnRequestPermissionsResult will be call in signup1activity
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    move_to_frag_show_image()
                } else {
                    val s = "You should accpect permission for chosing image from gallery"
                    s.showToastLong(mainInterfaceActivity)
                }
                return
            }
            else -> {
                val s = "Some thing went wrong. Please click again."
                s.showToastShort(mainInterfaceActivity)
                return
            }
        }
    }

    private fun move_to_frag_show_image() {
        replacefrag_in_main_interface_with_slide_in_left(
            tag = "frag_show_image_for_chosing_avatar",
            frag_show_image_for_chosing_avatar(),
            mainInterfaceActivity.supportFragmentManager
        )
    }

    override fun onItemSelected(position: Int, item: friend_in_profile) {

    }

}
