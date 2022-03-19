package theintership.my.signin_signup.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import theintership.my.MyMethod.Companion.isWifi
import theintership.my.MyMethod.Companion.replacefrag
import theintership.my.MyMethod.Companion.showToastLong
import theintership.my.R
import theintership.my.databinding.FragSigningBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_log_in_with_1_click
import theintership.my.signin_signup.dialog.dialog_show_account_and_password
import theintership.my.signin_signup.shareViewModel
import java.util.*


class frag_signing : Fragment(R.layout.frag_signing) {

    private var _binding: FragSigningBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private lateinit var database: DatabaseReference
    private val viewmodel: shareViewModel by activityViewModels()
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

        val account = viewmodel.account_user
        val password = viewmodel.password_user
        val dialogLogInWith1Click = dialog_log_in_with_1_click(signup1activity)

        dialogLogInWith1Click.show()

        dialogLogInWith1Click.btn_cancel.setOnClickListener {
            dialogLogInWith1Click.dismiss()
            open_dialog_show_account_and_password(account = account, password = password)
        }

        dialogLogInWith1Click.btn_save.setOnClickListener {
            val sharedPref = activity?.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE
            )
            if (sharedPref != null) {
                with(sharedPref.edit()) {
                    //Checking for show sign in view when user open app
                    putBoolean("User save password", true)
                    apply()
                }
            }
            dialogLogInWith1Click.dismiss()
            open_dialog_show_account_and_password(account = account, password = password)
        }


        return binding.root
    }


    private fun move_to_frag_authencation_account() {
        //User just need verify one of two ( phone number or email address )
        if (viewmodel.user_info.phone != "") {
            replacefrag(
                "frag_auth_phone_number_account",
                frag_auth_phone_number_account(),
                signup1activity.supportFragmentManager
            )
        } else {
            replacefrag(
                "frag_auth_email_address_account",
                frag_auth_email_address_account(),
                signup1activity.supportFragmentManager
            )
        }
    }

    private fun open_dialog_show_account_and_password(account: String, password: String) {
        val dialogshowAccountPassword = dialog_show_account_and_password(signup1activity)
        dialogshowAccountPassword.show()
        dialogshowAccountPassword.tv_account.text = viewmodel.account_user
        dialogshowAccountPassword.tv_password.text = viewmodel.password_user
        dialogshowAccountPassword.btn_go.setOnClickListener {
            signin_user_and_move_frag(account = account, password = password)
            dialogshowAccountPassword.dismiss()
        }
    }

    private fun error_network() {
        if (!isWifi(signup1activity)) {
            val s = "Connect wifi is disrupted , pls connect wifi and sign in again trong signing"
            s.showToastLong(signup1activity)

        } else {
            val s =
                "Some thing with our sever went wrong . Sorry for the error . Pls sign in again trong signing"
            s.showToastLong(signup1activity)
        }
    }

    private fun set_today(): String {
        var mtoday = Calendar.getInstance()
        var today = ""
        today += (mtoday.get(Calendar.MONTH) + 1).toString()
        today += "/"
        today += mtoday.get(Calendar.DAY_OF_MONTH).toString()
        today += "/"
        today += mtoday.get(Calendar.YEAR).toString()

        return today
    }

    private fun signin_user_and_move_frag(account: String, password: String) {
        val email = account + "@gmail.com"
        //If you concern about above line
        //See my explanation in frag_signup_creating_account in function create_auth_user_firebase
        CoroutineScope(Dispatchers.IO).launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val account_ref = viewmodel.account_user
                        val today = set_today()
                        val ref_last_login = database
                            .child("User")
                            .child(account_ref)
                            .child("user info")
                            .child("last login")

                        ref_last_login.setValue(today)
                            .addOnCompleteListener(signup1activity) { task2 ->
                                if (task2.isSuccessful) {
                                    move_to_frag_authencation_account()
                                } else {
                                    //Some thing wrong , it might be lost internet.
                                    // But just keep move frag . We can update later
                                    move_to_frag_authencation_account()
                                }
                            }
                    } else {
                        error_network()
                    }
                }
        }
    }


}