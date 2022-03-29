package theintership.my.signin_signup.fragment
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import theintership.my.R
import theintership.my.all_class.MyMethod.Companion.check_wifi
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.all_class.upload_image_by_putBytes_to_firebase
import theintership.my.all_class.upload_image_by_putFile_to_firebase
import theintership.my.databinding.FragDoneSetAvatarBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_loading
import theintership.my.signin_signup.shareViewModel
import kotlin.math.sign


class frag_done_set_avatar : Fragment(R.layout.frag_done_set_avatar) {

    private var _binding: FragDoneSetAvatarBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private val shareViewmodel: shareViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragDoneSetAvatarBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity

        if (!shareViewmodel.photo_user_null){
            binding.imageAvatarInDoneSetAvatar.setImageBitmap(shareViewmodel.photo_user)
        }

        binding.btnDoneSetAvatarBack.setOnClickListener {
            signup1activity.supportFragmentManager.popBackStack()
        }

        binding.btnDoneSetAvatarSave.setOnClickListener {
            if (!check_wifi(signup1activity)){
                return@setOnClickListener
            }
            val check = shareViewmodel.image_is_local_or_bitmap
            //true is user use local image
            //false is user use take photo
            if (check == "local"){
                upload_image_from_local()
            }
            if (check == "bitmap"){
                upload_image_from_take_photo()
            }
        }

        return binding.root
    }

    private fun upload_image_from_local(){
        val account_ref = shareViewmodel.account_user
        val image_path = shareViewmodel.image_path_from_local

        val dialogLoading = dialog_loading(signup1activity)
        dialogLoading.setCancelable(true)
        dialogLoading.show()

        val path_ref = "avatar_user/$account_ref"
        val upload2 = upload_image_by_putFile_to_firebase()
            .upload(path_image = image_path, path_ref = path_ref)

        upload2.addOnSuccessListener {
            dialogLoading.dismiss()
            val s = "Upload Success."
            s.showToastShort(signup1activity)
        }.addOnFailureListener {
            dialogLoading.dismiss()
            val s = "Please click again. My sever went wrong."
            s.showToastShort(signup1activity)
        }

    }

    private fun upload_image_from_take_photo(){
        val imageBitmap = shareViewmodel.photo_user

        val dialogLoading = dialog_loading(signup1activity)
        dialogLoading.setCancelable(true)
        dialogLoading.show()

        val account_ref = shareViewmodel.account_user
        val path_ref = "avatar_user/$account_ref"
        val upload2 = upload_image_by_putBytes_to_firebase()
            .upload(bitmap = imageBitmap, path_ref = path_ref)

        upload2.addOnSuccessListener {
            dialogLoading.dismiss()
            val s = "Upload Success."
            s.showToastShort(signup1activity)
        }.addOnFailureListener {
            dialogLoading.dismiss()
            val s = "Please take a photo again. My sever went wrong."
            s.showToastShort(signup1activity)
        }
    }

}
