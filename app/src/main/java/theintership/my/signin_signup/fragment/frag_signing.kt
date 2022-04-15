package theintership.my.signin_signup.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import theintership.my.all_class.MyMethod.Companion.check_wifi
import theintership.my.all_class.MyMethod.Companion.isWifi
import theintership.my.all_class.MyMethod.Companion.replacefrag
import theintership.my.all_class.MyMethod.Companion.showToastLong
import theintership.my.R
import theintership.my.databinding.FragSigningBinding
import theintership.my.Signup1Activity
import theintership.my.all_class.MyMethod.Companion.set_today
import theintership.my.signin_signup.dialog.dialog_log_in_with_1_click
import theintership.my.signin_signup.dialog.dialog_show_account_and_password
import theintership.my.signin_signup.shareViewModel
import java.util.*


class frag_signing : Fragment(R.layout.frag_signing) {

    private var _binding: FragSigningBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private lateinit var database: DatabaseReference
    private val shareViewModel: shareViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSigningBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity
        database = Firebase.database.reference
        auth = Firebase.auth
        auth.setLanguageCode("en")

        val account = shareViewModel.account_user
        val password = shareViewModel.password_user
        val dialogLogInWith1Click = dialog_log_in_with_1_click(signup1activity)

        dialogLogInWith1Click.show()

        dialogLogInWith1Click.btn_cancel.setOnClickListener {
            set_is_user_save_password_in_share_pref(save = false)
            dialogLogInWith1Click.dismiss()
            open_dialog_show_account_and_password(account = account, password = password)
        }

        dialogLogInWith1Click.btn_save.setOnClickListener {
            set_is_user_save_password_in_share_pref(save = true)
            dialogLogInWith1Click.dismiss()
            open_dialog_show_account_and_password(account = account, password = password)
        }


        return binding.root
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
        println("debug user email trong siging: ${user?.email.toString()}")
        if (user != null) {
            if (!user.isEmailVerified) {
                user.sendEmailVerification()
            }
        } else {
            error_network()
        }
    }

    private fun open_dialog_show_account_and_password(account: String, password: String) {
        val dialogshowAccountPassword = dialog_show_account_and_password(signup1activity)
        dialogshowAccountPassword.show()
        dialogshowAccountPassword.tv_account.text = shareViewModel.account_user
        dialogshowAccountPassword.tv_password.text = shareViewModel.password_user
        dialogshowAccountPassword.btn_go.setOnClickListener {
            if (!check_wifi(signup1activity)) {
                return@setOnClickListener
            }
            signin_user_and_move_frag(account = account, password = password)
            dialogshowAccountPassword.dismiss()
        }
    }

    private fun error_network() {
        if (!isWifi(signup1activity)) {
            val s = "Connect wifi is disrupted , pls connect wifi."
            s.showToastLong(signup1activity)

        } else {
            val s =
                "Some thing with our sever went wrong . Sorry for the error . Pls sign up again"
            s.showToastLong(signup1activity)
        }
    }


    private fun signin_user_and_move_frag(account: String, password: String) {
        val email = account + "@gmail.com"
        //If you concern about above line
        //See my explanation in frag_signup_creating_account in function create_auth_user_firebase
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val account_ref = shareViewModel.account_user
                            val today = set_today()
                            val ref_last_login = database
                                .child("User")
                                .child(account_ref)
                                .child("user info")
                                .child("last login")

                            ref_last_login.setValue(today)
                                .addOnCompleteListener(signup1activity) { task2 ->
                                    if (task2.isSuccessful) {
                                        move_to_frag_auth_email_address_account()
                                    } else {
                                        //Some thing wrong , it might be lost internet.
                                        // But just keep move frag . We can update later
                                        move_to_frag_auth_email_address_account()
                                    }
                                }
                        } else {
                            error_network()
                        }
                    }
            }
        }
    }


    private fun set_is_user_save_password_in_share_pref(save: Boolean) {
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val account = shareViewModel.account_user
        if (sharedPref != null) {
            with(sharedPref.edit()) {
                //Checking for show sign in view when user open app
                putBoolean("$account save password", save)
                apply()
            }
        }
    }

    private fun move_to_frag_auth_email_address_account() {
        update_email_user_and_verify_it()

        replacefrag(
            "frag_auth_email_address_account",
            frag_auth_email_address_account(),
            signup1activity.supportFragmentManager
        )
    }

}