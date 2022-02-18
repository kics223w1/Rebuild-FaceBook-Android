package theintership.my.signin_signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.databinding.FragSignupPhoneBinding

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
                binding.tvSignupPhoneInfo.text =
                    "Vui lòng nhập một số điện thoại hợp lệ hoặc dùng địa chỉ email của bạn."
                binding.tvSignupPhoneInfo.setTextColor(resources.getColor(R.color.error, null))
                return@setOnClickListener
            }
            binding.tvSignupPhoneInfo.text =
                "Nhập số di động để liên hệ của bạn. Bạn có thể ẩn thông tin này trên trang cá nhân sau."
            binding.tvSignupPhoneInfo.setTextColor(resources.getColor(R.color.light_grey, null))
            replacefrag(
                "frag_signup_password",
                frag_signup_password(),
                signup1Activity.supportFragmentManager
            )
        }



        return binding.root
    }
}