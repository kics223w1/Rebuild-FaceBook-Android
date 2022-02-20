package theintership.my.signin_signup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import theintership.my.R
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSignupEmailBinding
import theintership.my.signin_signup.Signup1Activity


class frag_signup_email : Fragment(R.layout.frag_signup_email) , IToast{

    private var _binding: FragSignupEmailBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupEmailBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity

        binding.btnSignupEmailBack.setOnClickListener {
            signup1activity.supportFragmentManager.popBackStack()
        }

        binding.btnSignupEmailGo.setOnClickListener {
            val email = binding.edtSignupEmail.text.toString()
            if (email == ""){
                show("Vui lòng nhập email" , signup1activity)
                return@setOnClickListener
            }

        }





        return binding.root
    }

}