package theintership.my.signin_signup

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.LeadingMarginSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSignupSexBinding

class frag_signup_sex : Fragment(R.layout.frag_signup_sex), IToast, IReplaceFrag {

    private var _binding: FragSignupSexBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupSexBinding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity

        binding.btnSignup4Back.setOnClickListener {
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

        binding.layoutFemale.setOnClickListener {
            binding.radioSignup4Female.isChecked = true
            binding.radioSignup4Male.isChecked = false
            binding.radioSignup4Custom.isChecked = false

            //remove layout custom
            binding.tvCustom2.visibility = View.VISIBLE
            binding.layoutCustom.visibility = View.GONE
            binding.tvCustom1.visibility = View.GONE
            binding.tvGender.visibility = View.GONE
            binding.edtGenderCustom.visibility = View.GONE
        }

        binding.layoutMale.setOnClickListener {
            binding.radioSignup4Male.isChecked = true
            binding.radioSignup4Female.isChecked = false
            binding.radioSignup4Custom.isChecked = false

            //remove layout custom
            binding.tvCustom2.visibility = View.VISIBLE
            binding.layoutCustom.visibility = View.GONE
            binding.tvCustom1.visibility = View.GONE
            binding.tvGender.visibility = View.GONE
            binding.edtGenderCustom.visibility = View.GONE
        }

        binding.layoutCustomSex.setOnClickListener {
            binding.radioSignup4Custom.isChecked = true
            binding.radioSignup4Female.isChecked = false
            binding.radioSignup4Male.isChecked = false

            //show layout custom
            binding.tvCustom2.visibility = View.GONE
            binding.layoutCustom.visibility = View.VISIBLE
            binding.tvCustom1.visibility = View.VISIBLE
            binding.tvGender.visibility = View.VISIBLE
        }

        binding.tvGender.setOnClickListener {
            binding.edtGenderCustom.visibility = View.VISIBLE
            binding.tvGender.visibility = View.GONE
        }

        binding.btnSignupSexGo.setOnClickListener {
            replacefrag(
                "frag_signup_phone",
                frag_signup_phone(),
                signup1Activity.supportFragmentManager
            )
        }


        binding.tvGender.setText(
            createIndentedText(
                "Nhập giới tính của bạn (không bắt buộc)",
                20,
                0
            )
        )
        binding.tvLayoutcustom.setText(createIndentedText("Cô ấy", 20, 0))


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

}