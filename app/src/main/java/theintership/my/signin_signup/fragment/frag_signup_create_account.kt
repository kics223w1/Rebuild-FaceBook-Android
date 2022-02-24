package theintership.my.signin_signup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.google.android.material.animation.AnimationUtils
import theintership.my.R
import theintership.my.databinding.FragSignupCreateAccountBinding
import theintership.my.signin_signup.Signup1Activity


class frag_signup_create_account : Fragment(R.layout.frag_signup_create_account) {

    private var _binding: FragSignupCreateAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupCreateAccountBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity

        binding.iconCreateAccountSuccess.animate().apply {
            duration = 1500
            scaleX(2.5F)
            scaleY(2.5F)
        }.withEndAction {
            binding.iconCreateAccountSuccess.animate().apply {
                duration = 1000
                scaleX(1.5F)
                scaleY(1.5F)
            }.withEndAction {
                binding.iconCreateAccountSuccess.animate().apply {
                    duration = 1500
                    scaleX(2.5F)
                    scaleY(2.5F)
                }.withEndAction{
                    binding.iconCreateAccountSuccess.animate().apply {
                        duration = 1000
                        scaleX(1.5F)
                        scaleY(1.5F)
                    }.start()
                }
            }
        }



        return binding.root
    }

}