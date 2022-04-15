package theintership.my.signin_signup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import theintership.my.all_class.MyMethod
import theintership.my.all_class.MyMethod.Companion.check_wifi
import theintership.my.all_class.MyMethod.Companion.hide_soft_key_board
import theintership.my.all_class.MyMethod.Companion.replacefrag
import theintership.my.all_class.MyMethod.Companion.showToastLong
import theintership.my.R
import theintership.my.databinding.FragChangeEmailWhenAuthBinding
import theintership.my.Signup1Activity
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
            if (check_email(email) && check_wifi(signup1activity)) {
                set_ref_email_and_go_to_frag_authencation_email(email)
            }
        }

        binding.edtChangeEmailWhenAuth.setOnEditorActionListener { textVieew, i, keyEvent ->
            val email = binding.edtChangeEmailWhenAuth.text.toString()
            hide_soft_key_board(signup1activity, binding.btnChangeEmailWhenAuthChange)

            if (check_email(email) && check_wifi(signup1activity)) {
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
        val old_email_address = shareViewModel.user_info.email.toString()
        if (old_email_address == email) {
            set_error_text_view("You just entered the emaill address which you entered when sign up.")
            return false
        }
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
        shareViewModel.is_email_address_change = true

        set_loading_process()
        println("debug index last: ${shareViewModel.index_of_last_ele_email_account}")

        var done_ref_phone_email_account = false
        var done_ref_user_info_email = false

        //These coroutine will death when fragment come to onDestroyView ( user move to next fragment )
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO){
                val ref_phone_and_email_and_account = database
                    .child("email and account")
                    .child(shareViewModel.index_of_last_ele_email_account.toString())
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
            }
        }
    }

    private fun go_to_frag_auth_email_address_account() {
        //Why i don't use popbackstack
        //Because when i use it , the phone didn't popback
        //The phone just reset the UI of this frag .
        //That is a bug i can't solve on March 19 2022.
        CoroutineScope(Dispatchers.IO).launch {
           update_email_user_and_verify_it()
        }
        replacefrag(
            "frag_auth_email_address_account",
            frag_auth_email_address_account(),
            signup1activity.supportFragmentManager
        )
    }

    private fun update_email_user_and_verify_it() {
        //My method for sign in in this app is SignInWithEmailAndPassword
        //You can see it in frag_signing
        //And you will see that i set email = account + "@gmail.com"
        //I have explained why i did that in frag_signing in function signin_user_and_move_frag

        //So firebase will understand that email of user is "user.account + @gmail.com"
        //So when we need to send a verification email to user
        //We need to update email of user to user.email that user has entered
        //When we done we just need reset it to the way it was

        val user = Firebase.auth.currentUser
        val email_address_for_verification = shareViewModel.user_info.email.toString()
        if (user == null){
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            user.updateEmail(email_address_for_verification)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        verify_user_email()
                    } else {
                        println("debug vao error network trong update email")
                        error_network()
                    }
                }
                .addOnFailureListener(signup1activity) {
                    println("debug update email e: $it")
                }
        }
    }



    private fun verify_user_email() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            if (!user.isEmailVerified) {
                user.sendEmailVerification()
            }
        } else {
            error_network()
        }
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