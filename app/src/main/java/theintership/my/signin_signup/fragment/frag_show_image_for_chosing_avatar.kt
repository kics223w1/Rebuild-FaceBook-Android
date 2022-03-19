package theintership.my.signin_signup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import theintership.my.R
import theintership.my.databinding.FragShowImageForChosingAvatarBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.base_adapter_image_gallery
import theintership.my.get_all_image_gallery

class frag_show_image_for_chosing_avatar : Fragment(R.layout.frag_show_image_for_chosing_avatar) {

    private var _binding : FragShowImageForChosingAvatarBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragShowImageForChosingAvatarBinding.inflate(inflater , container , false)
        signup1activity = activity as Signup1Activity
        val adapter = base_adapter_image_gallery(signup1activity)
        val list_image = get_all_image_gallery(signup1activity).getAllImage()

        println("debug size: ${list_image.size}")
        println("debug list: $list_image")


        adapter.setData(list_image)
        binding.gridViewFragShowImageForChoseAvatar.adapter = adapter

        binding.btnFragShowImageForChoseAvatarBack.setOnClickListener {
           signup1activity.supportFragmentManager.popBackStack()
        }

        binding.btnFragShowImageForChoseAvatarCamera.setOnClickListener {

        }



        return binding.root
    }

}