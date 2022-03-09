package theintership.my.signin_signup.fragment

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
import theintership.my.R
import theintership.my.`interface`.ICheckWifi
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSigningBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.viewModel_Signin_Signup


class frag_signing : Fragment(R.layout.frag_signing), IReplaceFrag, IToast,
    ICheckWifi {

    private var _binding: FragSigningBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private lateinit var database: DatabaseReference
    private val viewmodelSigninSignup: viewModel_Signin_Signup by activityViewModels()
    private val auth: FirebaseAuth = Firebase.auth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSigningBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity
        database = Firebase.database.reference
        val ref_user = database.child("User")
        val user = viewmodelSigninSignup.user_info
        val account_ref = viewmodelSigninSignup.account_user

        //Add user to ref on firebase realtime database
        ref_user.child(account_ref).setValue(user).addOnCompleteListener(signup1activity) { task ->
            if (task.isSuccessful) {
                val account = viewmodelSigninSignup.account_user
                val password = viewmodelSigninSignup.password_user
                signin_user_and_move_frag(account = account, password = password)
            }
        }



        return binding.root
    }


    private fun move_to_frag_authencation_account() {
        replacefrag(
            "frag_authencation_account",
            frag_authencation_account(),
            signup1activity.supportFragmentManager
        )
    }

    private fun error_network() {
        if (isWifi(signup1activity)) {
            showLong(
                "Connect wifi is disrupted , pls connect wifi and sign in again",
                signup1activity
            )
        } else {
            showLong(
                "Some thing with our sever went wrong . Sorry for the error . Pls sign in again",
                signup1activity
            )
        }
    }

    private fun signin_user_and_move_frag(account: String, password: String) {
        val email = account + "@gmail.com"
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(signup1activity) { task ->
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