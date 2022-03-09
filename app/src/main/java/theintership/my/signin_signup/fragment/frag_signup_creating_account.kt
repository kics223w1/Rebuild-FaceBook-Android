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
import com.google.firebase.ktx.Firebase
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSignupCreatingAccountBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.viewModel_Signin_Signup


class frag_signup_creating_account : Fragment(R.layout.frag_signup_creating_account), IReplaceFrag,
    IToast {

    private var _binding: FragSignupCreatingAccountBinding? = null
    private val binding get() = _binding!!
    private val signup1activity: Signup1Activity = activity as Signup1Activity
    private val viewmodelSigninSignup: viewModel_Signin_Signup by activityViewModels()
    private val auth: FirebaseAuth = Firebase.auth
    val dialog_stop_signup = dialog_stop_signup(signup1activity)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupCreatingAccountBinding.inflate(inflater, container, false)
        val email_user = viewmodelSigninSignup.user_info.email.toString()
        val password_user = viewmodelSigninSignup.password_user


        create_auth_user_firebase(email_user, password_user)


        binding.btnSignupCreatingAccountBackAndDelete.setOnClickListener {
            dialog_stop_signup.show()
            dialog_stop_signup.btn_cancel.setOnClickListener {
                startActivity(Intent(signup1activity, MainActivity::class.java))
                signup1activity.overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                dialog_stop_signup.dismiss()
            }
        }


        binding.btnSignupCreatingAccountBack.setOnClickListener {
            startActivity(Intent(signup1activity, MainActivity::class.java))
            signup1activity.overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
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
                    //Dismiss dialog if user click in button back and delete
                    dialog_stop_signup.dismiss()
                    show_icon_success_and_move()
               } else {
                    show("Create user fail", signup1activity)
                }
            }
    }

    private fun move_to_frag_signing() {
        replacefrag(
            "frag_signing_account",
            frag_signing(),
            signup1activity.supportFragmentManager
        )
    }


}