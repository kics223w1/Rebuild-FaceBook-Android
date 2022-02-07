package theintership.my.signin_signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSignup2Binding

class frag_signup2 : Fragment(R.layout.frag_signup2), IReplaceFrag , IToast {

    private var _binding: FragSignup2Binding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity
    var check_frag_signup3_1 = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignup2Binding.inflate(inflater , container , false)
        signup1Activity = activity as Signup1Activity
        var check = true

        show("$check_frag_signup3_1 trong pop ne" , signup1Activity)

        binding.btnSignup2Go.setOnClickListener {
            if (!check_frag_signup3_1){
                replacefrag(
                    tag = "frag_signup3_1",
                    frag = frag_signup3_1(),
                    fm = signup1Activity.supportFragmentManager
                )
                return@setOnClickListener
            }

            val firstname = binding.edtSignup2Fistname.text.toString()
            val lastname = binding.edtSignup2Lastname.text.toString()
            if(firstname == "" || lastname == ""){
                check = false
                binding.layoutSignup2Firstname.isErrorEnabled = true
                binding.layoutSignup2Firstname.error = "ok"
                binding.layoutSignup2Firstname.errorIconDrawable = null

                binding.layoutSignup2Lastname.isErrorEnabled = true
                binding.layoutSignup2Lastname.error = "ok"
                binding.layoutSignup2Lastname.errorIconDrawable = null

                binding.tvSignup2.setTextColor(resources.getColor(R.color.error , null))
                if (firstname == "" && lastname == "")
                    binding.tvSignup2.text = "Vui lòng nhập họ và tên của bạn"
                if (firstname == "" && lastname != "")
                    binding.tvSignup2.text = "Vui lòng nhập tên của bạn"
                if (firstname != "" && lastname == "")
                    binding.tvSignup2.text = "Vui lòng nhập họ của bạn"
                return@setOnClickListener
            }

            replacefrag(
                tag = "frag_signup3",
                frag = frag_signup3(),
                fm = signup1Activity.supportFragmentManager
            )

        }

        binding.edtSignup2Lastname.setOnEditorActionListener { textView, i, keyEvent ->
            val firstname = binding.edtSignup2Fistname.text.toString()
            val lastname = binding.edtSignup2Lastname.text.toString()
            if (i == EditorInfo.IME_ACTION_DONE && firstname != "" && lastname != ""){
                replacefrag(
                    tag = "frag_signup3",
                    frag = frag_signup3(),
                    fm = signup1Activity.supportFragmentManager
                )
                true
            }else{
                false
            }
        }

        binding.edtSignup2Fistname.doAfterTextChanged {
            if (!check){
                binding.tvSignup2.setTextColor(resources.getColor(R.color.light_grey , null))
                binding.tvSignup2.text = "Nhập tên bạn sử dụng trong đời thực"
                binding.layoutSignup2Firstname.isErrorEnabled = false
                binding.layoutSignup2Lastname.isErrorEnabled = false
                check = true
            }
        }

        binding.edtSignup2Lastname.doAfterTextChanged {
            if (!check){
                binding.tvSignup2.setTextColor(resources.getColor(R.color.light_grey , null))
                binding.tvSignup2.text = "Nhập tên bạn sử dụng trong đời thực"
                binding.layoutSignup2Firstname.isErrorEnabled = false
                binding.layoutSignup2Lastname.isErrorEnabled = false
                check = true
            }
        }

        binding.btnSignup2Back.setOnClickListener {
            val dialog = dialog(signup1Activity)
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