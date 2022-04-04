package theintership.my.signin_signup.fragment

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import theintership.my.R
import theintership.my.all_class.*
import theintership.my.all_class.MyMethod.Companion.check_wifi
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.databinding.FragShowImageForChosingAvatarBinding
import theintership.my.Signup1Activity
import theintership.my.signin_signup.adapter.IClickImage
import theintership.my.signin_signup.adapter.adapter_image
import theintership.my.signin_signup.shareViewModel


class frag_show_image_for_chosing_avatar : Fragment(R.layout.frag_show_image_for_chosing_avatar),
    IClickImage {

    private var _binding: FragShowImageForChosingAvatarBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private val shareViewModel: shareViewModel by activityViewModels()
    private val REQUEST_IMAGE_CAPTURE = 1
    private var image_path = ""
    private var check_image_path = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragShowImageForChosingAvatarBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity
        val rcv = binding.rcvShowImageForSelectingAvatar
        val list_image = get_all_image_gallery(signup1activity).getAllImage()

        val linearLayout: RecyclerView.LayoutManager = LinearLayoutManager(signup1activity)
        val adapter = adapter_image(this, signup1activity)
        adapter.submitList(list_image)

        rcv.layoutManager = linearLayout
        rcv.adapter = adapter

        if (list_image.size == 0){
            val s = "Not thing in your gallery."
            s.showToastShort(signup1activity)
        }

        binding.btnFragShowImageForChoseAvatarBack.setOnClickListener {
            signup1activity.supportFragmentManager.popBackStack()
        }

        binding.btnFragShowImageForChoseAvatarCamera.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding.btnFragShowImageForChoseAvatarDone.setOnClickListener {
            //User just can click that after click image
            if (!check_wifi(signup1activity)) {
                return@setOnClickListener
            }
            val bitmap_image = Compress_image_to_bitmap().compress(image_path = image_path)
            shareViewModel.image_is_local_or_bitmap = "local"
            shareViewModel.image_path_from_local = image_path

            //2 line below is for storing image to shareViewModel
            //So frag_done_set_avatar can take it easy
            shareViewModel.photo_user_null = false
            shareViewModel.photo_user = bitmap_image
            move_to_frag_done_set_avatar()
        }


        return binding.root
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            shareViewModel.photo_user = imageBitmap
            shareViewModel.photo_user_null = false
            shareViewModel.image_is_local_or_bitmap = "bitmap"
            move_to_frag_done_set_avatar()
        }
    }


    private fun move_to_frag_done_set_avatar() {
        MyMethod.replacefrag_by_silde_in_left(
            tag = "frag_done_set_avatar",
            frag_done_set_avatar(),
            signup1activity.supportFragmentManager
        )
    }


    override fun onClickImage(path: String) {
        if (check_image_path == path) {
            //User want to remove clicking from the image has been clicked
            binding.btnFragShowImageForChoseAvatarCamera.visibility = View.VISIBLE
            binding.btnFragShowImageForChoseAvatarDone.visibility = View.GONE
            check_image_path = ""
            return
        }

        shareViewModel.photo_user = Compress_image_to_bitmap().compress(path)
        shareViewModel.photo_user_null = false

        binding.btnFragShowImageForChoseAvatarCamera.visibility = View.GONE
        binding.btnFragShowImageForChoseAvatarDone.visibility = View.VISIBLE
        image_path = path
        check_image_path = path
    }
}