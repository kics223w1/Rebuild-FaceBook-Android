package theintership.my.signin_signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import theintership.my.R
import theintership.my.databinding.FragSignupPasswordBinding

class frag_signup_password : Fragment(R.layout.frag_signup_password) {

    private var _binding: FragSignupPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupPasswordBinding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity
        signup1Activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        binding.btnSignupPasswordGo.setOnClickListener {
            val password = binding.edtSignupPassword.text.toString()
            if (password.length < 6) {
                binding.layoutEdtSignupPassword.isErrorEnabled = true
                binding.layoutEdtSignupPassword.error = "ok"
                binding.layoutEdtSignupPassword.errorIconDrawable = null
                binding.tvSignupPasswordInfo.text =
                    "Mật khẩu của bạn phải có tối thiểu 6 chữ cái, số và biểu tượng (như ! và %%)."
                binding.tvSignupPasswordInfo.setTextColor(resources.getColor(R.color.error, null))
                return@setOnClickListener
            }
            binding.tvSignupPasswordInfo.text =
                "Tạo mật khẩu tối thiểu dài 6 ký tự. Đó phải là mật khẩu mà người khác không thể đoán được."
            binding.tvSignupPasswordInfo.setTextColor(resources.getColor(R.color.light_blue, null))
        }


        return binding.root
    }

}