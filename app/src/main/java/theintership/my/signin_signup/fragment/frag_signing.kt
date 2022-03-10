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
import theintership.my.MyMethod.Companion.isWifi
import theintership.my.MyMethod.Companion.replacefrag
import theintership.my.MyMethod.Companion.showToastLong
import theintership.my.R
import theintership.my.databinding.FragSigningBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_log_in_with_1_click
import theintership.my.signin_signup.dialog.dialog_show_account_and_password
import theintership.my.signin_signup.viewModel_Signin_Signup


class frag_signing : Fragment(R.layout.frag_signing){

    private var _binding: FragSigningBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private lateinit var database: DatabaseReference
    private val viewmodelSigninSignup: viewModel_Signin_Signup by activityViewModels()
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

        val account = viewmodelSigninSignup.account_user
        val password = viewmodelSigninSignup.password_user
        val dialogLogInWith1Click = dialog_log_in_with_1_click(signup1activity)

        dialogLogInWith1Click.show()
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
        replacefrag(
            "frag_auth_phone_number_account",
            frag_auth_phone_number_account(),
            signup1activity.supportFragmentManager
        )
    }

    private fun open_dialog_show_account_and_password(account: String, password: String) {
        val dialogshowAccountPassword = dialog_show_account_and_password(signup1activity)
        dialogshowAccountPassword.show()
        dialogshowAccountPassword.tv_account.text = viewmodelSigninSignup.account_user
        dialogshowAccountPassword.tv_password.text = viewmodelSigninSignup.password_user
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
            val s = "Some thing with our sever went wrong . Sorry for the error . Pls sign in again trong signing"
            s.showToastLong(signup1activity)
        }
    }

    private fun signin_user_and_move_frag(account: String, password: String) {
        val email = account + "@gmail.com"
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    move_to_frag_authencation_account()
                } else {
                    error_network()
                }
            }
    }


}