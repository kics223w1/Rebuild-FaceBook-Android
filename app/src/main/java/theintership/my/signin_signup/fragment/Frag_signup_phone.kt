package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.MainActivity
import theintership.my.MyMethod
import theintership.my.MyMethod.Companion.hide_soft_key_board
import theintership.my.MyMethod.Companion.replacefrag
import theintership.my.MyMethod.Companion.showToastLong
import theintership.my.R
import theintership.my.databinding.FragSignupPhoneBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.shareViewModel

class frag_signup_phone : Fragment(R.layout.frag_signup_phone) {

    private var _binding: FragSignupPhoneBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity
    private val shareViewModel: shareViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupPhoneBinding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity
        //signup1Activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        signup1Activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        binding.btnSignupPhoneGo.setOnClickListener {
            val phone = binding.edtSignupPhone.text.toString()
            val country_code = binding.edtSignupCountryCode.text.toString()
            hide_soft_key_board(signup1Activity, binding.btnSignupPhoneGo)
            if (validd_phone_number_and_country_code(phone = phone, country_code = country_code)) {
                goto_frag_signup_email(
                    phone = phone,
                    country_code = country_code
                )
            }

        }

        binding.edtSignupCountryCode.setOnEditorActionListener { textView, i, keyEvent ->
            val phone = binding.edtSignupPhone.text.toString()
            val country_code = binding.edtSignupCountryCode.text.toString()
            hide_soft_key_board(signup1Activity, binding.edtSignupCountryCode)
            if (validd_phone_number_and_country_code(phone = phone, country_code = country_code)) {
                goto_frag_signup_email(
                    phone = phone,
                    country_code = country_code
                )
                true
            } else {
                false
            }


        }

        binding.btnSignupPhoneEmailAddress.setOnClickListener {
            goto_frag_signup_email(
                phone = "",
                country_code = ""
            )
        }



        binding.btnSignupPhoneBack.setOnClickListener {
            hide_soft_key_board(signup1Activity, binding.btnSignupPhoneBack)
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

    private fun check_country_code(country_code: String): Boolean {
        if (country_code == "") {
            //User want to use the defaut VietNamese country code
            return true
        }
        if (country_code.length > 3 || country_code.length < 2) {
            return false
        }
        country_code.forEach {
            if (it !in '0'..'9') {
                return false
            }
        }
        return true
    }

    private fun set_error_edittext(str: String) {
        binding.tvSignupPhoneInfo.text = str
        binding.tvSignupPhoneInfo.setTextColor(resources.getColor(R.color.error, null))
    }

    private fun move_error_edittext() {
        binding.tvSignupPhoneInfo.text =
            "Please enter your phone number. You can always make this private later"
        binding.tvSignupPhoneInfo.setTextColor(resources.getColor(R.color.light_grey, null))
    }

    private fun goto_frag_signup_email(phone: String, country_code: String) {
        replacefrag(
            "frag_signup_email",
            frag_signup_email(),
            signup1Activity.supportFragmentManager
        )
        shareViewModel.set_user_info_phone(phone)
        if (country_code != "") {
            shareViewModel.set_user_info_country_code(country_code)
        }
    }

    private fun validd_phone_number_and_country_code(phone: String, country_code: String): Boolean {
        if (phone.length < 6) {
            set_error_edittext("Please enter valid phone number , like VietNamese phone number or you can use email address.")
            return false
        }
        if (!check_country_code(country_code)) {
            set_error_edittext("Please just enter valid country code\\n Please enter number in country code and don't enter '+' or charaters.")
            return false
        }
        val list_phone_number = shareViewModel.list_phone_number
        if (list_phone_number.contains(phone)) {
            set_error_edittext("Phone number already use by another user")
            return false
        }
        move_error_edittext()
        return true
    }

}