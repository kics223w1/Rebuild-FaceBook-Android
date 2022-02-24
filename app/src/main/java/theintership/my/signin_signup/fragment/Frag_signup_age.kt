package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSignupAgeBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_signup_age
import theintership.my.signin_signup.dialog.dialog_stop_signup
import kotlin.math.sign


class frag_signup_age : Fragment(R.layout.frag_signup_age), IReplaceFrag, IToast {

    private var _binding: FragSignupAgeBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupAgeBinding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity
        signup1Activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        signup1Activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        binding.btnSignupAgeGo.setOnClickListener {
            val text_age = binding.edtSignupAge.text.toString()
            if (text_age == ""){
               set_error_edittext()
               return@setOnClickListener
            }
            val age = text_age.toInt()
            if (age >= 118) {
                set_error_edittext()
                return@setOnClickListener
            }

            val dialog = dialog_signup_age(signup1Activity, age)
            dialog.show()
            dialog.btn_go.setOnClickListener {
                replacefrag(
                    "frag_signup_sex",
                    frag_signup_sex(),
                    signup1Activity.supportFragmentManager
                )
                signup1Activity.go_to_frag_signup_age = false
                dialog.dismiss()
            }
        }

        binding.edtSignupAge.setOnEditorActionListener { textView, i, KeyEvent ->
            val text_age = binding.edtSignupAge.text.toString()
            if (i != EditorInfo.IME_ACTION_DONE){
                false
            }
            if (text_age == ""){
                set_error_edittext()
                false
            }else{
                val age = text_age.toInt()
                if (age >= 118) {
                    set_error_edittext()
                    false
                }else{
                    val dialog = dialog_signup_age(signup1Activity, age)
                    dialog.show()
                    dialog.btn_go.setOnClickListener {
                        replacefrag(
                            "frag_signup_sex",
                            frag_signup_sex(),
                            signup1Activity.supportFragmentManager
                        )
                        signup1Activity.go_to_frag_signup_age = false
                        dialog.dismiss()
                    }
                    true
                }
            }
       }

        binding.btnSignupAgePopback.setOnClickListener {
            val size = signup1Activity.supportFragmentManager.backStackEntryCount
            val frag_after_fragAge = signup1Activity.supportFragmentManager.getBackStackEntryAt(size - 2)
            //size always >= 2

            if (frag_after_fragAge.name == "frag_signup_name") {
                //User go to frag_signup_age from frag_signup_name , so need to replacefrag
                signup1Activity.supportFragmentManager.popBackStack()
                replacefrag(
                    "frag_signup_birthday",
                    frag_signup_birthday(),
                    signup1Activity.supportFragmentManager
                )
                signup1Activity.go_to_frag_signup_age = false
                return@setOnClickListener
            }

            signup1Activity.go_to_frag_signup_age = false
            signup1Activity.supportFragmentManager.popBackStack()
        }

        binding.btnSignupAgeBack.setOnClickListener {
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

    fun set_error_edittext(){
        binding.tvSignupAgeInfoError.visibility = View.VISIBLE
        binding.layoutEdtSignupAge.isErrorEnabled = true
        binding.layoutEdtSignupAge.error = "ok"
        binding.layoutEdtSignupAge.errorIconDrawable = null
    }

}