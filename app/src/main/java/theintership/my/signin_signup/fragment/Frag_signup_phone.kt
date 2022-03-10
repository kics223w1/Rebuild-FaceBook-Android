package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import theintership.my.MainActivity
import theintership.my.MyMethod.Companion.replacefrag
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSignupPhoneBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.viewModel_Signin_Signup

class frag_signup_phone : Fragment(R.layout.frag_signup_phone) {

    private var _binding: FragSignupPhoneBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity
    private val viewModel_Signin_Signup : viewModel_Signin_Signup by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupPhoneBinding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity
        signup1Activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        signup1Activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        binding.btnSignupPhoneGo.setOnClickListener {
            val phone = binding.edtSignupPhone.text.toString()
            if (phone.length < 6) {
                set_error_edittext("Please enter valid phone number , like VietNamese phone number or you can use email address.")
                return@setOnClickListener
            }
            val list_phone_number = viewModel_Signin_Signup.list_phone_number
            if (list_phone_number.contains(phone)){
                set_error_edittext("Phone number already use by another user")
                return@setOnClickListener
            }
            move_error_edittext()
            goto_frag_signup_password(phone)
        }

        binding.edtSignupPhone.setOnEditorActionListener { textView, i, keyEvent ->
            val phone = binding.edtSignupPhone.text.toString()
            val list_phone_number = viewModel_Signin_Signup.list_phone_number
            if (phone.length < 6){
                set_error_edittext("Please enter valid phone number , like VietNamese phone number or you can use email address.")
                false
            }else if(list_phone_number.contains(phone)){
                set_error_edittext("Phone number already use by another user")
                false
            } else {
                move_error_edittext()
                goto_frag_signup_password(phone)
                true
            }
        }

        binding.btnSignupPhoneEmailAddress.setOnClickListener {
            goto_frag_signup_password("")
        }



        binding.btnSignupPhoneBack.setOnClickListener {
            val dialog = dialog_stop_signup(signup1Activity)
            dialog.show()
            dialog.btn_cancel.setOnClickListener {
                startActivity(Intent(signup1Activity, MainActivity::class.java))
                signup1Activity.overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                dialog.dismiss()
            }
        }

        return binding.root
    }

    fun set_error_edittext(str : String) {
        binding.tvSignupPhoneInfo.text =
            ""
        binding.tvSignupPhoneInfo.setTextColor(resources.getColor(R.color.error, null))
    }

    fun move_error_edittext() {
        binding.tvSignupPhoneInfo.text =
            "Please enter your phone number. You can always make this private later"
        binding.tvSignupPhoneInfo.setTextColor(resources.getColor(R.color.light_grey, null))
    }

    fun goto_frag_signup_password(phone : String){
        replacefrag(
            "frag_signup_email",
            frag_signup_email(),
            signup1Activity.supportFragmentManager
        )
        viewModel_Signin_Signup.set_user_info_phone(phone)
    }
}