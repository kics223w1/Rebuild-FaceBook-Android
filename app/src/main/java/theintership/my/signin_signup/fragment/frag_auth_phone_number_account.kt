package theintership.my.signin_signup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import theintership.my.R
import theintership.my.databinding.FragAuthPhoneNumberAccountBinding
import theintership.my.signin_signup.Signup1Activity


class frag_auth_phone_number_account : Fragment(R.layout.frag_auth_phone_number_account) {

    private var _binding: FragAuthPhoneNumberAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragAuthPhoneNumberAccountBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity





        return binding.root
    }

}