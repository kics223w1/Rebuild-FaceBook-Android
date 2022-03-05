package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.databinding.FragSignupPasswordBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.viewModel_Signin_Signup

class frag_signup_password : Fragment(R.layout.frag_signup_password), IReplaceFrag {

    private var _binding: FragSignupPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity
    private val viewmodelSigninSignup : viewModel_Signin_Signup by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupPasswordBinding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity


        binding.btnSignupPasswordGo.setOnClickListener {
            val password = binding.edtSignupPassword.text.toString()
            if (password.length < 6) {
                val s = "Mật khẩu của bạn phải có tối thiểu 6 chữ cái, " +
                        "số và biểu tượng (như ! và %%)."
                set_error_edittext(s)
                return@setOnClickListener
            }

            if (is_same(password)) {
                val s = "Vui lòng chọn một mật khẩu an toàn hơn. " +
                        "Mật khẩu phải dài hơn 6 ký tự, " +
                        "chỉ riêng bạn biết và người khác khó có thể đoán được."
                set_error_edittext(s)
                return@setOnClickListener
            }

            move_error_edittext()
            goto_frag_signup_email(password)
        }

        binding.edtSignupPassword.setOnEditorActionListener { textView, i, keyEvent ->
            val password = binding.edtSignupPassword.text.toString()
            if (password.length < 6) {
                val s = "Mật khẩu của bạn phải có tối thiểu 6 chữ cái, " +
                        "số và biểu tượng (như ! và %%)."
                set_error_edittext(s)
                false
            }

            if (is_same(password)) {
                val s = "Vui lòng chọn một mật khẩu an toàn hơn. " +
                        "Mật khẩu phải dài hơn 6 ký tự, " +
                        "chỉ riêng bạn biết và người khác khó có thể đoán được."
                set_error_edittext(s)
                false
            }

            move_error_edittext()
            goto_frag_signup_email(password)
            true
        }

        binding.btnSignupPaswordBack.setOnClickListener {
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

    fun is_same(password: String): Boolean {
        for (i in 1 until password.length) {
            if (password[i] != password[i - 1]) {
                return false
            }
        }
        return true
    }

    fun set_error_edittext(s: String) {
        binding.layoutEdtSignupPassword.isErrorEnabled = true
        binding.layoutEdtSignupPassword.error = "ok"
        binding.layoutEdtSignupPassword.errorIconDrawable = null
        binding.tvSignupPasswordInfo.text = s
        binding.tvSignupPasswordInfo.setTextColor(resources.getColor(R.color.error, null))
    }

    fun goto_frag_signup_email(password: String){
        replacefrag(
            "frag_signup_email",
            frag_signup_email(),
            signup1Activity.supportFragmentManager
        )
        viewmodelSigninSignup.password = password
    }

    fun move_error_edittext(){
        binding.tvSignupPasswordInfo.text =
            "Tạo mật khẩu tối thiểu dài 6 ký tự. " +
                    "Đó phải là mật khẩu mà người khác không thể đoán được."
        binding.tvSignupPasswordInfo.setTextColor(resources.getColor(R.color.light_blue, null))
    }

}