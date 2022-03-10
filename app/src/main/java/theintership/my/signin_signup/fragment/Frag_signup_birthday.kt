package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import theintership.my.MainActivity
import theintership.my.MyMethod.Companion.replacefrag
import theintership.my.R
import theintership.my.databinding.FragSignupBirthdayBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.viewModel_Signin_Signup
import java.util.*

class frag_signup_birthday : Fragment(R.layout.frag_signup_birthday) {

    private var _binding: FragSignupBirthdayBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity
    private val viewModel_Signin_Signup: viewModel_Signin_Signup by activityViewModels()


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
        var birthday = ""

        binding.datePickerSignupBirthday.maxDate = today.timeInMillis
        binding.datePickerSignupBirthday.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            age = today.get(Calendar.YEAR) - year
            binding.tvSignupBirthdayShowage.text = "${age} years old"
            birthday = day.toString() + "/" + month.toString() + "/" + year.toString()
        }


        binding.btnSignupBirthdayGo.setOnClickListener {
            if (age > 4) {
                move_to_frag_sex(age , birthday)
                check_user_want_to_go_frag_age = false
                return@setOnClickListener
            }
            if (check_user_want_to_go_frag_age) {
                move_to_frag_age()
                signup1Activity.go_to_frag_signup_age = true
                check_user_want_to_go_frag_age = false
                return@setOnClickListener
            }

            if (age <= 4) {
                binding.tvSignupBirthdayInfo.text =
                    "It look like you entered the wrong info. Please be sure to user your real birthday"
                binding.tvSignupBirthdayInfo.setTextColor(resources.getColor(R.color.error, null))
                if (age == -1) {
                    binding.tvSignupBirthdayShowage.text = "One years old"
                }
                check_user_want_to_go_frag_age = true
                return@setOnClickListener
            }
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

    private fun move_to_frag_sex(age: Int , birthday : String) {
        replacefrag(
            "frag_signup_sex",
            frag_signup_sex(),
            signup1Activity.supportFragmentManager
        )
        viewModel_Signin_Signup.set_user_info_birthday(birthday = birthday)
        viewModel_Signin_Signup.set_user_info_age(age)
    }

    private fun move_to_frag_age(){
        replacefrag(
            "frag_signup_age",
            frag_signup_age(),
            signup1Activity.supportFragmentManager
        )
    }

}