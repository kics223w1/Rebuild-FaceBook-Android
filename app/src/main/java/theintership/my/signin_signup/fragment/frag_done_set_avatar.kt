package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import theintership.my.Main_Interface_Activity
import theintership.my.R
import theintership.my.all_class.MyMethod.Companion.check_wifi
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.all_class.upload_image_by_putBytes_to_firebase
import theintership.my.all_class.upload_image_by_putFile_to_firebase
import theintership.my.Signup1Activity
import theintership.my.all_class.GetUri_Image_Firebase
import theintership.my.databinding.FragDoneSetAvatarBinding
import theintership.my.signin_signup.model.category_privacy_avatar
import theintership.my.signin_signup.adapter.adapter_category_privacy_avatar
import theintership.my.signin_signup.dialog.dialog_loading
import theintership.my.signin_signup.shareViewModel


class frag_done_set_avatar : Fragment(R.layout.frag_done_set_avatar) {

    private var _binding: FragDoneSetAvatarBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private val shareViewmodel: shareViewModel by activityViewModels()
    private lateinit var database: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragDoneSetAvatarBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity
        database = Firebase.database.reference
        var privacy = ""

        val adapter = adapter_category_privacy_avatar(
            signup1activity,
            R.layout.select_category_privacy_avatar,
            getList_category()
        )
        binding.spinnerDoneSetAvatar.adapter = adapter
        binding.spinnerDoneSetAvatar.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    privacy = adapter.getItem(p2)?.name.toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }


        if (!shareViewmodel.photo_user_null) {
            binding.imageAvatarInDoneSetAvatar.setImageBitmap(shareViewmodel.photo_user)
        }

        binding.btnDoneSetAvatarBack.setOnClickListener {
            signup1activity.supportFragmentManager.popBackStack()
        }

        binding.btnDoneSetAvatarSave.setOnClickListener {
            if (!check_wifi(signup1activity)) {
                return@setOnClickListener
            }
            binding.btnDoneSetAvatarSave.visibility = View.INVISIBLE
            binding.progressDoneSetAvatarSave.visibility = View.VISIBLE

            val check = shareViewmodel.image_is_local_or_bitmap
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                withContext(Dispatchers.IO) {
                    if (check == "local") {
                        upload_image_from_local()
                    }
                    if (check == "bitmap") {
                        upload_image_from_take_photo()
                    }
                }
            }
        }

        return binding.root
    }


    private fun getList_category(): MutableList<category_privacy_avatar> {
        var list = mutableListOf<category_privacy_avatar>()
        val a = category_privacy_avatar("Everyone", R.drawable.everyone)
        val b = category_privacy_avatar("Friends", R.drawable.groupfriend)
        val c = category_privacy_avatar("Just you", R.drawable.justyou)
        list.add(a)
        list.add(b)
        list.add(c)

        return list
    }


    private fun upload_image_from_local() {
        val account_ref = shareViewmodel.account_user
        val image_path = shareViewmodel.image_path_from_local

        var path_ref = "avatar_user/"
        path_ref += account_ref.toString()
        val upload2 = upload_image_by_putFile_to_firebase()
            .upload(path_image = image_path, path_ref = path_ref)

        upload2.addOnSuccessListener {
            val s = "Upload Success."
            s.showToastShort(signup1activity)
            getUriImage_andUpdateDatabase()
        }.addOnFailureListener {
            val s = "Please click again. My sever went wrong."
            s.showToastShort(signup1activity)
        }

    }

    private fun upload_image_from_take_photo() {
        val imageBitmap = shareViewmodel.photo_user

        val account_ref = shareViewmodel.account_user
        println("debug account ref trong upload : $account_ref")
        var path_ref = "avatar_user/"
        path_ref += account_ref.toString()
        val upload2 = upload_image_by_putBytes_to_firebase()
            .upload(bitmap = imageBitmap, path_ref = path_ref)

        upload2.addOnSuccessListener {
            val s = "Upload Success."
            s.showToastShort(signup1activity)
            getUriImage_andUpdateDatabase()
        }.addOnFailureListener {
            val s = "Please take a photo again. My sever went wrong."
            s.showToastShort(signup1activity)
        }
    }

    private fun getUriImage_andUpdateDatabase() {
        val account_ref = shareViewmodel.account_user
        val ref = "avatar_user/$account_ref"
        val ref_user = database
            .child("User")
            .child(account_ref)
            .child("link avatar")
        GetUri_Image_Firebase().getUri(ref).addOnSuccessListener {
            ref_user.setValue(it.toString()).addOnCompleteListener(signup1activity) { task ->
                if (task.isSuccessful) {
                    go_to_main_interface()
                } else {
                   error()
                }
            }
        }.addOnFailureListener {
            println("debug e in get uri frag done set avatar: $it")
           error()
        }
    }

    private fun error(){
        val check = shareViewmodel.image_is_local_or_bitmap
        if (check == "local") {
            val s = "Please click again. My sever went wrong."
            s.showToastShort(signup1activity)
        } else {
            val s = "Please take a photo again. My sever went wrong."
            s.showToastShort(signup1activity)
        }
    }

    private fun go_to_main_interface() {
        startActivity(Intent(activity, Main_Interface_Activity::class.java))
        activity?.overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        activity?.finish()
    }

}
