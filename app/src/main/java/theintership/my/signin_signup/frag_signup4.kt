package theintership.my.signin_signup

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
import theintership.my.databinding.FragSignup4Binding

class frag_signup4 : Fragment(R.layout.frag_signup4), IToast, IReplaceFrag {

    private var _binding: FragSignup4Binding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignup4Binding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity

        binding.btnSignup4Back.setOnClickListener {
            mainActivity.supportFragmentManager.popBackStack()
        }

        binding.radioSignup4Female.setOnClickListener {
            binding.radioSignup4Male.isChecked = false
            binding.radioSignup4Custom.isChecked = false

            //remove layout custom
            binding.tvCustom2.visibility = View.VISIBLE
            binding.layoutCustom.visibility = View.GONE
            binding.tvCustom1.visibility = View.GONE
            binding.tvGender.visibility = View.GONE
            binding.edtGenderCustom.visibility = View.GONE
        }

        binding.radioSignup4Male.setOnClickListener {
            binding.radioSignup4Female.isChecked = false
            binding.radioSignup4Custom.isChecked = false

            //remove layout custom
            binding.tvCustom2.visibility = View.VISIBLE
            binding.layoutCustom.visibility = View.GONE
            binding.tvCustom1.visibility = View.GONE
            binding.tvGender.visibility = View.GONE
            binding.edtGenderCustom.visibility = View.GONE
        }

        binding.radioSignup4Custom.setOnClickListener {
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

        binding.tvGender.setText(createIndentedText("Nhập giới tính của bạn (không bắt buộc)" , 20 , 0))
        binding.tvLayoutcustom.setText(createIndentedText("Cô ấy" , 20 , 0))


        return binding.root
    }

    fun createIndentedText(text: String, marginFirstLine: Int, marginNextLines: Int): SpannableString {
        var result = SpannableString(text)
        result.setSpan(LeadingMarginSpan.Standard(marginFirstLine, marginNextLines), 0, text.length, 0)
        return result
    }

}