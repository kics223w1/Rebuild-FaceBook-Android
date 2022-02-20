package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.databinding.FragSignupPhoneBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup

class frag_signup_phone : Fragment(R.layout.frag_signup_phone), IReplaceFrag {

    private var _binding: FragSignupPhoneBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity

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
            if (phone.length <= 2) {
                set_error_edittext()
                return@setOnClickListener
            }
            move_error_edittext()
            goto_frag_signup_email()
        }

        binding.btnSignupPhoneGo.setOnEditorActionListener { textView, i, keyEvent ->
            val phone = binding.edtSignupPhone.text.toString()
            if (phone.length <= 2) {
                set_error_edittext()
                false
            } else {
                move_error_edittext()
                goto_frag_signup_email()
                true
            }
        }

        binding.btnSignupPhoneEmail.setOnClickListener {
            goto_frag_signup_email()
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

    fun set_error_edittext() {
        binding.tvSignupPhoneInfo.text =
            "Vui lòng nhập một số điện thoại hợp lệ hoặc dùng địa chỉ email của bạn."
        binding.tvSignupPhoneInfo.setTextColor(resources.getColor(R.color.error, null))
    }

    fun move_error_edittext() {
        binding.tvSignupPhoneInfo.text =
            "Nhập số di động để liên hệ của bạn. Bạn có thể ẩn thông tin này trên trang cá nhân sau."
        binding.tvSignupPhoneInfo.setTextColor(resources.getColor(R.color.light_grey, null))
    }

    fun goto_frag_signup_email(){
        replacefrag(
            "frag_signup_email",
            frag_signup_email(),
            signup1Activity.supportFragmentManager
        )
    }
}