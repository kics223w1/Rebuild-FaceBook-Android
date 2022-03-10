package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.MainActivity
import theintership.my.MyMethod.Companion.isWifi
import theintership.my.MyMethod.Companion.replacefrag
import theintership.my.MyMethod.Companion.showToastLong
import theintership.my.R
import theintership.my.`interface`.ICheckWifi
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSignupCreatingAccountBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.viewModel_Signin_Signup


class frag_signup_creating_account : Fragment(R.layout.frag_signup_creating_account) {

    private var _binding: FragSignupCreatingAccountBinding? = null
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
        _binding = FragSignupCreatingAccountBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity
        database = Firebase.database.reference
        val email_user = viewmodelSigninSignup.user_info.email.toString()
        val password_user = viewmodelSigninSignup.password_user

        create_auth_user_firebase(email_user, password_user)


        binding.btnSignupCreatingAccountBackAndDelete.setOnClickListener {
            val s = "Can't back when creating account"
            s.showToastLong(signup1activity)
        }

        return binding.root
    }

    private fun show_icon_success_and_move() {
        binding.progressCreatingAccount.visibility = View.GONE
        binding.tvFragSignupCreatingAccountInfo.visibility = View.GONE
        binding.iconCreatingAccountSuccess.visibility = View.VISIBLE
        binding.iconCreatingAccountSuccess.animate().apply {
            duration = 1500
            scaleX(2.5F)
            scaleY(2.5F)
        }.withEndAction {
            binding.iconCreatingAccountSuccess.animate().apply {
                duration = 1000
                scaleX(1.5F)
                scaleY(1.5F)
            }.withEndAction {
                binding.iconCreatingAccountSuccess.animate().apply {
                    duration = 1500
                    scaleX(2.5F)
                    scaleY(2.5F)
                }.withEndAction {
                    binding.iconCreatingAccountSuccess.animate().apply {
                        duration = 1000
                        scaleX(1.5F)
                        scaleY(1.5F)
                    }.start()
                }
            }
        }
        Handler().postDelayed({
            move_to_frag_signing()
        }, 5000)
    }

    private fun create_auth_user_firebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(signup1activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    show_icon_success_and_move()
                } else {
                    error_network()
                }
            }
    }

    private fun error_network() {
        if (!isWifi(signup1activity)) {
            val s = "Connect wifi is disrupted , pls connect wifi."
            s.showToastLong(signup1activity)
        } else {
            val s =
                "Some thing with our sever went wrong . Sorry for the error . Pls sign up again."
            s.showToastLong(signup1activity)
            //Delete user_info and phone_email_account in firebase realtime database
            //After delete user can sign up again with same infomation
            delete_user_info_and_phoneEmailAccount_and_move_frag()
        }
    }

    private fun move_to_frag_signing() {
        replacefrag(
            "frag_signing_account",
            frag_signing(),
            signup1activity.supportFragmentManager
        )
    }


    private fun delete_user_info_and_phoneEmailAccount_and_move_frag() {
        var delete_user = false
        var delete_phone_email_account = false

        //Deletet user_info
        val account_ref = viewmodelSigninSignup.account_user
        val ref_user = database.child("User").child(account_ref).child("user info")
        ref_user.removeValue().addOnCompleteListener(signup1activity) { task ->
            if (task.isSuccessful) {
                delete_user = true
                if (delete_user && delete_phone_email_account) {
                    move_to_frag_signing()
                }
            }
        }
        // Delete phone and email and account
        val ref_phoneEmailAccount = database.child("phone and email and account")
        var id = viewmodelSigninSignup.index_of_last_ele_phone_email_account
        if (id != -1) {
            ref_phoneEmailAccount.child(id.toString()).removeValue()
                .addOnCompleteListener(signup1activity) { task ->
                    if (task.isSuccessful) {
                        delete_phone_email_account = true
                        if (delete_user && delete_phone_email_account) {
                            move_to_frag_signing()
                        }
                    }
                }
        } else {
            delete_phone_email_account = true
            if (delete_user && delete_phone_email_account) {
                move_to_frag_signing()
            }
        }
    }
}