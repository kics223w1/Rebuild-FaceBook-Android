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
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSignupCreatingAccountBinding
import theintership.my.model.User
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.viewModel_Signin_Signup
import java.util.concurrent.ThreadPoolExecutor


class frag_signup_creating_account : Fragment(R.layout.frag_signup_creating_account), IReplaceFrag,
    IToast {

    private var _binding: FragSignupCreatingAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private val viewmodelSigninSignup: viewModel_Signin_Signup by activityViewModels()
    private val auth: FirebaseAuth = Firebase.auth




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupCreatingAccountBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity
        val user = viewmodelSigninSignup.User
        val phone_user = user.phone
        val email_user = user.email
        val same_person = viewmodelSigninSignup.same_person

        if (phone_user == "" && email_user == "" && same_person) {

        }

        if (phone_user != "" && email_user != "") {
            add_user_and_phone_email(user)
        }

        binding.btnSignupCreatingAccountBack.setOnClickListener {
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

    private fun add_user_and_phone_email(muser: User) {
        val email = muser.email.toString()
        val password = viewmodelSigninSignup.password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(signup1activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    show_icon_success_and_move()

//                    CoroutineScope(Dispatchers.IO).launch {
//                        ref_user.child(email_ref).setValue(muser)
//                        ref_phone_and_email.child(email_ref).setValue(muser)
//                    }

                    //The above function will make this fragment go to onPause and
                    //that will reset the view so the function show_icon_success_move will repeat two times ,
                    // so we must implement the above function in frag_signing
                } else {
                    // If sign in fails, display a message to the user.
                    show("Create user fail", signup1activity)
                }
            }
    }


    private fun move_to_frag_signing() {
        replacefrag(
            "frag_signing_account",
            frag_signing_account(),
            signup1activity.supportFragmentManager
        )
    }

}