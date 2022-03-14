package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.MainActivity
import theintership.my.MyMethod.Companion.hide_soft_key_board
import theintership.my.MyMethod.Companion.isWifi
import theintership.my.MyMethod.Companion.replacefrag
import theintership.my.MyMethod.Companion.showToastLong
import theintership.my.R
import theintership.my.databinding.FragSignupEmailBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.shareViewModel
import kotlin.math.sign


class frag_signup_email : Fragment(R.layout.frag_signup_email) {

    private var _binding: FragSignupEmailBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private var database: DatabaseReference = Firebase.database.reference
    private val shareViewModel: shareViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupEmailBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity
        var user_change_email_address_when_verify =
            shareViewModel.is_user_change_email_when_authencation

        if (user_change_email_address_when_verify) {
            //Change some UI to make user easy to understand
            binding.btnSignupEmailPhoneNumber.visibility = View.GONE
            binding.tvSignupEmailInfo.text = "Please enter new email address you want to change."
        }

        binding.btnSignupEmailBack.setOnClickListener {
            signup1activity.supportFragmentManager.popBackStack()
        }

        binding.btnSignupEmailGo.setOnClickListener {
            val email = binding.edtSignupEmail.text.toString()
            hide_soft_key_board(signup1activity, binding.btnSignupEmailGo)
            val size = signup1activity.supportFragmentManager.backStackEntryCount
            for (i in 0 until size){
                println("debug stack: ${signup1activity.supportFragmentManager.getBackStackEntryAt(i).name}")
            }
            println("")


            if (!check_email(email)) {
                return@setOnClickListener
            }
            if (user_change_email_address_when_verify) {
                set_ref_email_and_go_to_frag_authencation_email(email)
            }else{
                goto_frag_account(email)
            }
        }

        binding.edtSignupEmail.setOnEditorActionListener { textview, i, keyevent ->
            val email = binding.edtSignupEmail.text.toString()
            hide_soft_key_board(signup1activity, binding.edtSignupEmail)
            if (!check_email(email)) {
                false
            } else if (user_change_email_address_when_verify) {
                set_ref_email_and_go_to_frag_authencation_email(email)
                true
            } else {
                goto_frag_account(email)
                true
            }
        }

        binding.btnSignupEmailPhoneNumber.setOnClickListener {
            hide_soft_key_board(signup1activity, binding.btnSignupEmailPhoneNumber)
            signup1activity.supportFragmentManager.popBackStack()
        }

        binding.btnSignupEmailBack.setOnClickListener {
            hide_soft_key_board(signup1activity, binding.btnSignupEmailBack)
            if (user_change_email_address_when_verify) {
                shareViewModel.is_user_change_email_when_authencation = false
                signup1activity.supportFragmentManager.popBackStack()
                return@setOnClickListener
            }
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

    private fun check_email(email: String): Boolean {
        if (email == "") {
            set_error_text_view("Enter email please")
            return false
        }
        if (!email.contains("@gmail.com")) {
            set_error_text_view("Email must be the format @gmail.com\n Example: huyhuy@gmail.com")
            return false
        }
        //if email has @gmail.com so it length is >= 10
        //Make sure email.length >= 10 for continuing
        val check_fomat = email.subSequence(email.length - 10, email.length)
        if (check_fomat != "@gmail.com") {
            set_error_text_view("Email must be the format @gmail.com\n Example: huyhuy@gmail.com")
            return false
        }
        for (i in 0 until email.length - 10) {
            val it = email[i]
            if (it !in 'a'..'z' && it !in 'A'..'Z' && it !in '0'..'9') {
                set_error_text_view("Email can not contain $it \nor space , just contain characters")
                return false
            }
        }
        val list_email_address = shareViewModel.list_email_address
        if (list_email_address.contains(email)) {
            set_error_text_view("Email address already use by another user")
            return false
        }
        move_error_text_view()
        return true
    }

    private fun goto_frag_account(email: String) {
        shareViewModel.set_user_info_email(email = email)
        replacefrag(
            "frag_signup_account",
            frag_signup_account(),
            signup1activity.supportFragmentManager
        )
    }

    private fun goto_frag_authencation_email(email: String) {
        shareViewModel.set_user_info_email(email = email)
        replacefrag(
            "frag_auth_email_address_account",
            frag_auth_email_address_account(),
            signup1activity.supportFragmentManager
        )
    }

    private fun set_ref_email_and_go_to_frag_authencation_email(email: String) {


        //Add new email address to list and delete the old one
        //And set new email address in user info
        val old_email_address = shareViewModel.user_info.email
        shareViewModel.list_email_address.add(email)
        shareViewModel.list_email_address.remove(old_email_address)
        shareViewModel.set_user_info_email(email = email)
        shareViewModel.is_user_change_email_when_authencation = false

        var done_ref_phone_email_account = false
        var done_ref_user_info_email = false

        val ref_phone_and_email_and_account = database
            .child("phone and email and account")
            .child(shareViewModel.index_of_last_ele_phone_email_account.toString())
            .child("email")
        ref_phone_and_email_and_account.setValue(email)
            .addOnCompleteListener(signup1activity) { task ->
                if (task.isSuccessful) {
                    done_ref_phone_email_account = true
                    if (done_ref_phone_email_account && done_ref_user_info_email) {
                        signup1activity.supportFragmentManager.popBackStack()
                    }
                } else {
                    error_network()
                }
            }

        val account_ref = shareViewModel.account_user
        val ref_user_info_email = database
            .child("User")
            .child(account_ref)
            .child("user info")
            .child("email")
        ref_user_info_email.setValue(email).addOnCompleteListener(signup1activity) { task ->
            if (task.isSuccessful) {
                done_ref_user_info_email = true
                if (done_ref_phone_email_account && done_ref_user_info_email) {
                    signup1activity.supportFragmentManager.popBackStack()
                }
            } else {
                error_network()
            }
        }
    }

    private fun set_error_text_view(str: String) {
        binding.tvSignupEmailInfo.text = str
        binding.tvSignupEmailInfo.setTextColor(resources.getColor(R.color.error, null))
    }

    private fun move_error_text_view() {
        binding.tvSignupEmailInfo.text =
            "Adding an email to keep your account secure, find friends and more"
        binding.tvSignupEmailInfo.setTextColor(resources.getColor(R.color.light_grey, null))
    }

    private fun error_network() {
        if (!isWifi(signup1activity)) {
            val s = "Please connect wifi and click button Next again."
            s.showToastLong(signup1activity)
        } else {
            val s = "Some thing went wrong with our sever. Please click button Next again."
            s.showToastLong(signup1activity)
        }
    }
}