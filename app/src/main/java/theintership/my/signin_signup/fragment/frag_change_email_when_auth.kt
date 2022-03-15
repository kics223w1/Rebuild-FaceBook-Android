package theintership.my.signin_signup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch
import theintership.my.MyMethod
import theintership.my.MyMethod.Companion.hide_soft_key_board
import theintership.my.MyMethod.Companion.replacefrag
import theintership.my.MyMethod.Companion.showToastLong
import theintership.my.R
import theintership.my.databinding.FragAuthPhoneNumberAccountBinding
import theintership.my.databinding.FragChangeEmailWhenAuthBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.shareViewModel

class frag_change_email_when_auth : Fragment(R.layout.frag_change_email_when_auth) {

    private var _binding: FragChangeEmailWhenAuthBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private var database: DatabaseReference = Firebase.database.reference
    private val shareViewModel: shareViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragChangeEmailWhenAuthBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity


        binding.btnChangeEmailWhenAuthBack.setOnClickListener {
            hide_soft_key_board(signup1activity, binding.btnChangeEmailWhenAuthBack)
            signup1activity.supportFragmentManager.popBackStack()
        }

        binding.btnChangeEmailWhenAuthChange.setOnClickListener {
            val email = binding.edtChangeEmailWhenAuth.text.toString()
            hide_soft_key_board(signup1activity, binding.btnChangeEmailWhenAuthChange)
            if (check_email(email)) {
                set_ref_email_and_go_to_frag_authencation_email(email)
            }
        }

        binding.edtChangeEmailWhenAuth.setOnEditorActionListener { textVieew, i, keyEvent ->
            val email = binding.edtChangeEmailWhenAuth.text.toString()
            hide_soft_key_board(signup1activity, binding.btnChangeEmailWhenAuthChange)

            if (check_email(email)) {
                set_ref_email_and_go_to_frag_authencation_email(email)
                true
            } else {
                false
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


    private fun set_ref_email_and_go_to_frag_authencation_email(email: String) {
        //Add new email address to list and delete the old one
        //And set new email address in user info
        val old_email_address = shareViewModel.user_info.email
        shareViewModel.list_email_address.add(email)
        shareViewModel.list_email_address.remove(old_email_address)
        shareViewModel.set_user_info_email(email = email)

        set_loading_process()

        var done_ref_phone_email_account = false
        var done_ref_user_info_email = false

        //These coroutine will death when fragment come to onDestroyView ( user move to next fragment )
        // CoroutineScope(Dispatchers.IO).launch {
        val ref_phone_and_email_and_account = database
            .child("phone and email and account")
            .child(shareViewModel.index_of_last_ele_phone_email_account.toString())
            .child("email")
        ref_phone_and_email_and_account.setValue(email)
            .addOnCompleteListener(signup1activity) { task ->
                if (task.isSuccessful) {
                    done_ref_phone_email_account = true
                    if (done_ref_phone_email_account && done_ref_user_info_email) {
                        remove_loading_process()
                        go_to_frag_auth_email_address_account()
                    }
                } else {
                    error_network()
                }
            }
        //}
        //CoroutineScope(Dispatchers.IO).launch {
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
                        remove_loading_process()
                        go_to_frag_auth_email_address_account()
                    }
            } else {
                error_network()
            }
        }
        //}

    }

    private fun go_to_frag_auth_email_address_account() {
        replacefrag(
            "frag_auth_email_address_account",
            frag_auth_email_address_account(),
            signup1activity.supportFragmentManager
        )
    }

    private fun set_error_text_view(str: String) {
        binding.tvChangeEmailWhenAuthInfo.text = str
        binding.tvChangeEmailWhenAuthInfo.setTextColor(resources.getColor(R.color.error, null))
    }

    private fun move_error_text_view() {
        binding.tvChangeEmailWhenAuthInfo.text =
            "Enter the email address that you want to change."
        binding.tvChangeEmailWhenAuthInfo.setTextColor(resources.getColor(R.color.light_grey, null))
    }

    private fun set_loading_process() {
        binding.progressChangeEmailWhenAuth.visibility = View.VISIBLE
        binding.btnChangeEmailWhenAuthChange.visibility = View.INVISIBLE
    }

    private fun remove_loading_process() {
        binding.btnChangeEmailWhenAuthChange.visibility = View.VISIBLE
        binding.progressChangeEmailWhenAuth.visibility = View.INVISIBLE
    }

    private fun error_network() {
        if (!MyMethod.isWifi(signup1activity)) {
            val s = "Please connect wifi and click button Change email address again."
            s.showToastLong(signup1activity)
        } else {
            val s =
                "Some thing went wrong with our sever. Please click button Change email address again."
            s.showToastLong(signup1activity)
        }
    }
}