package theintership.my.signin_signup.fragment
import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import theintership.my.MainActivity
import theintership.my.Main_Interface_Activity
import theintership.my.all_class.MyMethod.Companion.check_wifi
import theintership.my.all_class.MyMethod.Companion.replacefrag_by_silde_in_left
import theintership.my.all_class.MyMethod.Companion.showToastLong
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.R
import theintership.my.all_class.MyMethod.Companion.replacefrag
import theintership.my.databinding.FragSetAvatarBinding
import theintership.my.Signup1Activity
import theintership.my.signin_signup.shareViewModel
import theintership.my.all_class.upload_image_by_putBytes_to_firebase


class frag_set_avatar : Fragment(R.layout.frag_set_avatar) {

    private var _binding: FragSetAvatarBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private val REQUEST_CODE = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var imageView: ImageView
    private val shareViewmodel: shareViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSetAvatarBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity
        imageView = binding.imageViewTest

        binding.btnFragSetAvatarChoseFromGallery.setOnClickListener {
            check_permission_read_image()
        }


        binding.btnFragSetAvatarTakeAPhoto.setOnClickListener {
            if (!check_wifi(signup1activity)) {
                return@setOnClickListener
            }
            dispatchTakePictureIntent()
        }

        binding.btnFragSetAvatarSkip.setOnClickListener {
            if (!check_wifi(signup1activity)) {
                return@setOnClickListener
            }
            update_default_avatar_to_firebase()
        }

        return binding.root
    }


    private fun update_default_avatar_to_firebase() {
        binding.btnFragSetAvatarSkip.visibility = View.GONE
        binding.progressFragSetAvatar.visibility = View.VISIBLE

        var sex = shareViewmodel.user_info.sex
        if (sex == "") {
            sex = shareViewmodel.user_info.gender
        }
        var bitmap_image = createBitmap(1000, 1000)
        if (sex == "Female" || sex == "She") {
            bitmap_image = BitmapFactory.decodeResource(
                signup1activity.resources,
                R.drawable.default_female_avatar
            )
        } else {
            bitmap_image = BitmapFactory.decodeResource(
                signup1activity.resources,
                R.drawable.default_male_avatar
            )
        }
        val path_ref = "avatar_user/${shareViewmodel.account_user}"
        upload_image_by_putBytes_to_firebase().upload(
            bitmap = bitmap_image,
            path_ref = path_ref
        ).addOnSuccessListener {
            go_to_main_interface()
        }.addOnFailureListener {
            binding.btnFragSetAvatarSkip.visibility = View.VISIBLE
            binding.progressFragSetAvatar.visibility = View.GONE
            val s = "Please click again. My sever went wrong."
            s.showToastShort(signup1activity)
        }
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            println("debug e in dispatchTakePictureIntent: $e")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            shareViewmodel.photo_user = imageBitmap
            shareViewmodel.photo_user_null = false
            shareViewmodel.image_is_local_or_bitmap = "bitmap"
            move_to_frag_done_set_avatar()
        }
    }


    private fun check_permission_read_image() {
        when {
            ContextCompat.checkSelfPermission(
                signup1activity,
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
                    s.showToastLong(signup1activity)
                }
                return
            }
            else -> {
                val s = "Some thing went wrong. Please click again."
                s.showToastShort(signup1activity)
                return
            }
        }
    }

    private fun go_to_main_interface(){
        startActivity(Intent(activity, Main_Interface_Activity::class.java))
        activity?.overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        activity?.finish()
    }


    private fun move_to_frag_done_set_avatar() {
        replacefrag(
            tag = "frag_done_set_avatar",
            frag_done_set_avatar(),
            signup1activity.supportFragmentManager
        )
    }


    private fun move_to_frag_show_image() {
        replacefrag_by_silde_in_left(
            tag = "frag_show_image_for_chosing_avatar",
            frag_show_image_for_chosing_avatar(),
            signup1activity.supportFragmentManager
        )
    }

}
