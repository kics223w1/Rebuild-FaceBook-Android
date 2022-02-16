package theintership.my.signin_signup

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
import java.util.*

class frag_signup_birthday : Fragment(R.layout.frag_signup_birthday), IReplaceFrag , IToast {

    private var _binding: FragSignupBirthdayBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity
    var check = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupBirthdayBinding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity

        val today = Calendar.getInstance()
        var age = -1
        binding.datePickerSignup3.maxDate = today.timeInMillis
        binding.datePickerSignup3.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            age = today.get(Calendar.YEAR) - year
            if (age > 4 && month == today.get(Calendar.MONTH) && day == today.get(Calendar.DAY_OF_MONTH)){
                binding.tvSignup3Showage.text = "Happy Birthday :))"
            }else{
                binding.tvSignup3Showage.text = age.toString() + " tuổi"
            }
        }


        binding.btnSignup3Go.setOnClickListener {
            if (!check) {
                check = true
                replacefrag(
                    "frag_signup3_1",
                    frag_signup_age(),
                    signup1Activity.supportFragmentManager
                )
                return@setOnClickListener
            }
            if (age <= 4) {
                binding.tvSignup3Info.text =
                    "Hình như bạn đã nhập sai thông tin. Hãy nhớ dùng ngày sinh nhật thật của mình nhé."
                binding.tvSignup3Info.setTextColor(resources.getColor(R.color.error, null))
                if (age == -1) binding.tvSignup3Showage.text = "1 tuổi"
                check = false
                return@setOnClickListener
            }

            check = true
            replacefrag(
                tag = "frag_signup4",
                frag = frag_signup_sex(),
                fm = signup1Activity.supportFragmentManager
            )
        }

        binding.btnSignup3Back.setOnClickListener {
            val dialog = dialog_cancel_signup(signup1Activity)
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