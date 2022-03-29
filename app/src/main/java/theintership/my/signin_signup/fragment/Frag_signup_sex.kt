package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.LeadingMarginSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.MainActivity
import theintership.my.all_class.MyMethod.Companion.hide_soft_key_board
import theintership.my.all_class.MyMethod.Companion.replacefrag
import theintership.my.R
import theintership.my.databinding.FragSignupSexBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_bottom_sex
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.shareViewModel

class frag_signup_sex : Fragment(R.layout.frag_signup_sex) {

    private var _binding: FragSignupSexBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity
    private val shareViewModel: shareViewModel by activityViewModels()
    private var database: DatabaseReference = Firebase.database.reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupSexBinding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity

        binding.btnSignupSexBack.setOnClickListener {
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

        binding.layoutSignupSexFemale.setOnClickListener {
            binding.radioSignupSexFemale.isChecked = true
            binding.radioSignupSexMale.isChecked = false
            binding.radioSignupSexCustom.isChecked = false

            //remove layout custom
            binding.tvCustom2.visibility = View.VISIBLE
            binding.layoutCustom.visibility = View.GONE
            binding.tvCustom1.visibility = View.GONE
            binding.tvGender.visibility = View.GONE
            binding.edtGenderCustom.visibility = View.GONE
        }

        binding.layoutSignupSexMale.setOnClickListener {
            binding.radioSignupSexMale.isChecked = true
            binding.radioSignupSexFemale.isChecked = false
            binding.radioSignupSexCustom.isChecked = false

            //remove layout custom
            binding.tvCustom2.visibility = View.VISIBLE
            binding.layoutCustom.visibility = View.GONE
            binding.tvCustom1.visibility = View.GONE
            binding.tvGender.visibility = View.GONE
            binding.edtGenderCustom.visibility = View.GONE
        }

        binding.layoutSignupSexCustom.setOnClickListener {
            binding.radioSignupSexCustom.isChecked = true
            binding.radioSignupSexFemale.isChecked = false
            binding.radioSignupSexMale.isChecked = false

            //show layout custom
            binding.tvCustom2.visibility = View.GONE
            binding.layoutCustom.visibility = View.VISIBLE
            binding.tvCustom1.visibility = View.VISIBLE
            binding.tvGender.visibility = View.VISIBLE
        }

        binding.layoutCustom.setOnClickListener {
            val dialog = dialog_bottom_sex(signup1Activity)
            dialog.show()
            dialog.layout_her.setOnClickListener {
                binding.tvLayoutcustomInfo.text = "She"
                dialog.dismiss()
            }
            dialog.layout_him.setOnClickListener {
                binding.tvLayoutcustomInfo.text = "He"
                dialog.dismiss()
            }
            dialog.layout_them.setOnClickListener {
                binding.tvLayoutcustomInfo.text = "They"
                dialog.dismiss()
            }

        }

        binding.tvGender.setOnClickListener {
            binding.edtGenderCustom.visibility = View.VISIBLE
            binding.tvGender.visibility = View.GONE
        }

        binding.btnSignupSexGo.setOnClickListener {
            if (!check_select_sex()) {
                if (binding.radioSignupSexCustom.isChecked) {
                    binding.tvFragSignupSexInfo.text = "Please select your pronoun"
                    binding.tvFragSignupSexInfo.setTextColor(
                        resources.getColor(
                            R.color.error,
                            null
                        )
                    )
                    binding.tvLayoutcustomInfo.setTextColor(resources.getColor(R.color.error, null))
                } else {
                    binding.tvFragSignupSexInfo.text = "Please select your gender"
                    binding.tvFragSignupSexInfo.setTextColor(
                        resources.getColor(
                            R.color.error,
                            null
                        )
                    )
                    binding.tvLayoutCustom.setTextColor(resources.getColor(R.color.error, null))
                    binding.tvLayoutMale.setTextColor(resources.getColor(R.color.error, null))
                    binding.tvLayoutFemale.setTextColor(resources.getColor(R.color.error, null))
                }
                return@setOnClickListener
            }
            val sex = take_sex()
            val pronoun = take_pronoun()
            val gender = binding.edtGenderCustom.text.toString()
            move_to_frag_phone(sex, pronoun, gender, binding.btnSignupSexGo)
        }

        binding.btnSignupSexBack.setOnClickListener {
            hide_soft_key_board(signup1Activity, binding.btnSignupSexBack)
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

        binding.tvGender.setText(
            createIndentedText(
                "Please enter your gender (optional)",
                20,
                0
            )
        )
        binding.tvLayoutcustomInfo.setText(createIndentedText("Select your pronoun", 20, 0))


        return binding.root
    }

    fun createIndentedText(
        text: String,
        marginFirstLine: Int,
        marginNextLines: Int
    ): SpannableString {
        var result = SpannableString(text)
        result.setSpan(
            LeadingMarginSpan.Standard(marginFirstLine, marginNextLines),
            0,
            text.length,
            0
        )
        return result
    }

    private fun check_select_sex(): Boolean {
        if (binding.radioSignupSexFemale.isChecked) return true
        if (binding.radioSignupSexMale.isChecked) return true
        if (binding.radioSignupSexCustom.isChecked) {
            val text = binding.tvLayoutcustomInfo.text.toString()
            if (text != "Select your pronoun") return true
        }
        return false
    }

    private fun take_sex(): String {
        if (binding.radioSignupSexMale.isChecked) return "Male"
        if (binding.radioSignupSexFemale.isChecked) return "Female"
        if (binding.radioSignupSexCustom.isChecked) return "Custom"
        return ""
    }

    private fun take_pronoun(): String {
        if (binding.radioSignupSexCustom.isChecked) {
            val pronoun = binding.tvLayoutcustomInfo.text.toString()
            return pronoun
        }
        return ""
    }

    private fun move_to_frag_phone(
        sex: String,
        pronoun: String,
        gender: String,
        view: View
    ) {
        shareViewModel.set_user_info_sex(sex)
        shareViewModel.set_user_info_pronoun(pronoun)
        shareViewModel.set_user_info_gender(gender)
        hide_soft_key_board(signup1Activity, view)
        replacefrag(
            "frag_signup_phone",
            frag_signup_phone(),
            signup1Activity.supportFragmentManager
        )
    }

}