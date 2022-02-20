package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSignupBirthdayBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import java.util.*

class frag_signup_birthday : Fragment(R.layout.frag_signup_birthday), IReplaceFrag, IToast {

    private var _binding: FragSignupBirthdayBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupBirthdayBinding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity
        var check_user_want_to_go_frag_age = false
        val today = Calendar.getInstance()
        var age = -1

        binding.datePickerSignupBirthday.maxDate = today.timeInMillis
        binding.datePickerSignupBirthday.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            age = today.get(Calendar.YEAR) - year
            binding.tvSignupBirthdayShowage.text = "${age} tuổi"
        }


        binding.btnSignupBirthdayGo.setOnClickListener {
            if (check_user_want_to_go_frag_age) {
                replacefrag(
                    "frag_signup_age",
                    frag_signup_age(),
                    signup1Activity.supportFragmentManager
                )
                signup1Activity.go_to_frag_signup_age = true
                check_user_want_to_go_frag_age = false
                return@setOnClickListener
            }

            if (age <= 4) {
                binding.tvSignupBirthdayInfo.text =
                    "Hình như bạn đã nhập sai thông tin. Hãy nhớ dùng ngày sinh nhật thật của mình nhé."
                binding.tvSignupBirthdayInfo.setTextColor(resources.getColor(R.color.error, null))
                if (age == -1) binding.tvSignupBirthdayShowage.text = "1 tuổi"
                check_user_want_to_go_frag_age = true
                return@setOnClickListener
            }

            replacefrag(
                tag = "frag_signup4",
                frag = frag_signup_sex(),
                fm = signup1Activity.supportFragmentManager
            )
        }

        binding.btnSignupBirthdayBack.setOnClickListener {
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

}