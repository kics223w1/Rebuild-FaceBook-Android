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
import theintership.my.all_class.Dowload_image_from_Firebase_by_dowloadURL
import theintership.my.all_class.MyMethod.Companion.showToastLong
import theintership.my.databinding.FragDoneSetAvatarBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.shareViewModel
import java.io.ByteArrayOutputStream


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

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            withContext(Dispatchers.IO) {
                val account_ref = shareViewmodel.account_user
                val path_ref = "avatar_user/$account_ref"
                Dowload_image_from_Firebase_by_dowloadURL().dowload(path_ref = path_ref)
                    .addOnSuccessListener {
                        set_image(it)
                    }.addOnFailureListener {
                        val s = "Sorry my sever went wrong so i can't show you your avatar." +
                                "But don't worry , your avatar has been in my sever, so please keep conitnue."
                        s.showToastLong(signup1activity)
                    }
            }
        }

        binding.btnDoneSetAvatarBack.setOnClickListener {
            signup1activity.supportFragmentManager.popBackStack()
        }




        return binding.root
    }

    fun set_image(image: Uri) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            withContext(Dispatchers.Main) {
                Glide.with(signup1activity).load(image).error(R.drawable.error_image)
                    .into(binding.imageAvatarInDoneSetAvatar)
            }
        }
    }

}