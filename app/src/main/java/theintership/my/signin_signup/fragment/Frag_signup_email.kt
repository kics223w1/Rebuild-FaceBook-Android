package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSignupEmailBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.viewModel_Signin_Signup


class frag_signup_email : Fragment(R.layout.frag_signup_email), IToast, IReplaceFrag {

    private var _binding: FragSignupEmailBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private val viewModel_Signin_Signup: viewModel_Signin_Signup by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupEmailBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity

        binding.btnSignupEmailBack.setOnClickListener {
            signup1activity.supportFragmentManager.popBackStack()
        }

        binding.btnSignupEmailGo.setOnClickListener {
            val email = binding.edtSignupEmail.text.toString()
            if (!check_email(email)){
                return@setOnClickListener
            }
            goto_frag_done(email)
        }

        binding.edtSignupEmail.setOnEditorActionListener{
            textview , i , keyevent ->
            val email = binding.edtSignupEmail.text.toString()
            if (!check_email(email)){
                println("debug vao false trong editemail")
                false
            }else{
                if (goto_frag_done(email)){
                    println("debug vao true edtemail")
                    true
                }else{
                    false
                }
            }
        }

        binding.btnSignupEmailPass.setOnClickListener {
            goto_frag_done("")
        }

        binding.btnSignupEmailBack.setOnClickListener {
            val dialog = dialog_stop_signup(signup1activity)
            dialog.show()
            dialog.btn_cancel.setOnClickListener {
                startActivity(Intent(signup1activity, MainActivity::class.java))
                signup1activity.overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                dialog.dismiss()
            }
        }



        return binding.root
    }

    private fun check_email(email : String) : Boolean{
        if (email == "") {
            show("Vui lòng nhập email", signup1activity)
            return false
        }
        email.forEach {
            if (it == ' '){
                show("Email không thể có khoảng trắng" , signup1activity)
                return false
            }
        }
        if (!email.contains("@gmail.com")){
            showLong("Email phải là email định dạng @gmail.com\n ví dụ: huyhuy@gmail.com" , signup1activity)
            return false
        }
        return true
    }

    private fun goto_frag_done(email :String) : Boolean{
        replacefrag(
            "frag_signup_done",
            frag_signup_done(),
            signup1activity.supportFragmentManager
        )
        viewModel_Signin_Signup.set_user_email(email = email)
        return true
    }
}