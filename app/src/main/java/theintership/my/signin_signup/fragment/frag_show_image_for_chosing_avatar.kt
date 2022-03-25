package theintership.my.signin_signup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import theintership.my.MyMethod.Companion.showToastShort
import theintership.my.R
import theintership.my.databinding.FragShowImageForChosingAvatarBinding
import theintership.my.get_all_image_gallery
import theintership.my.model.image
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.adapter.IClickImage
import theintership.my.signin_signup.adapter.adapter_image

class frag_show_image_for_chosing_avatar : Fragment(R.layout.frag_show_image_for_chosing_avatar) , IClickImage {

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
        val rcv=  binding.rcvShowImageForSelectingAvatar
        val list_image = get_all_image_gallery(signup1activity).getAllImage()

        val linearLayout : RecyclerView.LayoutManager = LinearLayoutManager(signup1activity)
        val adapter = adapter_image(this, signup1activity)
        adapter.submitList(list_image)

        rcv.layoutManager = linearLayout
        rcv.adapter = adapter

        return binding.root
    }

    override fun onClickImage(path: String) {
        path.showToastShort(signup1activity)
    }
}