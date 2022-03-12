package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.DatabaseReference
import theintership.my.MainActivity
import theintership.my.MyMethod.Companion.replacefrag
import theintership.my.R
import theintership.my.databinding.FragSignupAccountBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.viewModel_Signin_Signup

class frag_signup_account : Fragment(R.layout.frag_signup_account) {


    private var _binding: FragSignupAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity
    private val viewmodelSigninSignup: viewModel_Signin_Signup by activityViewModels()
    private lateinit var database: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupAccountBinding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity


        binding.btnSignupAccountGo.setOnClickListener {
            val account = binding.edtSignupAccount.text.toString()
            if (check_account(account)) {
                go_to_frag_password(account)
            }
        }

        binding.edtSignupAccount.setOnEditorActionListener { textView, i, keyEvent ->
            val account = binding.edtSignupAccount.text.toString()
            if (check_account(account)) {
                go_to_frag_password(account)
                true
            }else{
                false
            }
        }



        binding.btnSignupAccountBack.setOnClickListener {
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


    private fun go_to_frag_password(account: String) {
        viewmodelSigninSignup.account_user = account
        replacefrag(
            "frag_signup_password",
            frag_signup_password(),
            signup1Activity.supportFragmentManager
        )
    }


    private fun set_error_text_view(str: String) {
        binding.tvSignupAccountInfo.text = str
        binding.tvSignupAccountInfo.setTextColor(resources.getColor(R.color.error, null))
    }

    private fun move_error_text_view() {
        val str =
            "Create account with at least 6 characters. It should be something others couldn't guess."
        binding.tvSignupAccountInfo.text = str
        binding.tvSignupAccountInfo.setTextColor(resources.getColor(R.color.error, null))
    }

    private fun check_account(account: String): Boolean {
        if (account == "") {
            set_error_text_view("Create account with at least 6 characters. It should be something others couldn't guess.")
            return false
        }
        account.forEach {
            if (it !in 'a'..'z' && it !in 'A'..'Z' && it !in '0'..'9') {
                set_error_text_view("Account can just contain characters and number.\n Can't contain $it")
                return false
            }
        }
        val list_account = viewmodelSigninSignup.list_account
        println("debug list: $list_account")
        println("debug account: $account")
        if (list_account.contains(account)){
            set_error_text_view("Account already use by another user")
            return false
        }
        move_error_text_view()
        return true
    }

}