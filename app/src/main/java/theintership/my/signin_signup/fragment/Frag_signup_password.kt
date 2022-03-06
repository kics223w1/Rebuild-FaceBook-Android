package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.databinding.FragSignupPasswordBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.viewModel_Signin_Signup

class frag_signup_password : Fragment(R.layout.frag_signup_password), IReplaceFrag {

    private var _binding: FragSignupPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity
    private val viewmodelSigninSignup: viewModel_Signin_Signup by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupPasswordBinding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity


        binding.btnSignupPasswordGo.setOnClickListener {
            val password = binding.edtSignupPassword.text.toString()
            if (!valid_password(password)) {
                val s = "Your password must has at least 6 characters " +
                        "number or symbol (! and %)."
                set_error_edittext(s)
                return@setOnClickListener
            }

            if (is_same_password(password)) {
                val s = "Please chose a more secure password. " +
                        "It should be longer than 6 characters " +
                        "unique to you, and difficult to other to guess."
                set_error_edittext(s)
                return@setOnClickListener
            }

            move_error_edittext()
            goto_frag_signup_email(password)
        }

        binding.edtSignupPassword.setOnEditorActionListener { textView, i, keyEvent ->
            val password = binding.edtSignupPassword.text.toString()
            if (!valid_password(password)) {
                val s = "Your password must has at least 6 characters " +
                        "number or symbol (like ! and % ) or space."
                set_error_edittext(s)
                false
            } else if (is_same_password(password)) {
                val s = "Please chose a mode secure password. " +
                        "It should be longer than 6 characters " +
                        "unique to you, and difficult to other to guess."
                set_error_edittext(s)
                false
            } else {
                move_error_edittext()
                goto_frag_signup_email(password)
                true
            }
        }

        binding.btnSignupPaswordBack.setOnClickListener {
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

    fun valid_password(password: String): Boolean {
        if (password.length < 6){
            return false
        }
        for (i in 0 until password.length) {
            if (password[i] in 'a'..'z' || password[i] in 'A'..'Z'
                || password[i] == '!' || password[i] == '%' || password[i] == ' ') {
                continue
            } else {
                return false
            }
        }
        return true
    }

    fun is_same_password(password: String): Boolean {
        for (i in 1 until password.length) {
            if (password[i] != password[i - 1]) {
                return false
            }
        }
        return true
    }

    fun set_error_edittext(s: String) {
        binding.layoutEdtSignupPassword.isErrorEnabled = true
        binding.layoutEdtSignupPassword.error = "ok"
        binding.layoutEdtSignupPassword.errorIconDrawable = null
        binding.tvSignupPasswordInfo.text = s
        binding.tvSignupPasswordInfo.setTextColor(resources.getColor(R.color.error, null))
    }

    fun goto_frag_signup_email(password: String) {
        replacefrag(
            "frag_signup_email",
            frag_signup_email(),
            signup1Activity.supportFragmentManager
        )
        viewmodelSigninSignup.password = password
    }

    fun move_error_edittext() {
        binding.tvSignupPasswordInfo.text =
            "Your password must has at least 6 characters. " +
                    "It should be something other couldn't guess."
        binding.tvSignupPasswordInfo.setTextColor(resources.getColor(R.color.light_blue, null))
    }

}