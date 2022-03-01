package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.databinding.FragSignupCreatingAccountBinding
import theintership.my.model.User
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.viewModel_Signin_Signup


class frag_signup_creating_account : Fragment(R.layout.frag_signup_creating_account), IReplaceFrag {

    private var _binding: FragSignupCreatingAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private val viewmodelSigninSignup: viewModel_Signin_Signup by activityViewModels()
    private var database: DatabaseReference = Firebase.database.reference

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


    private fun add_user_and_phone_email(user: User) {
        val ref_user = database.child("User")
        val ref_phone_and_email = database.child("phone and email").orderByKey().limitToLast(1)
        ref_phone_and_email.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val last_ele = snapshot.children
                var mindex = "0"
                last_ele.forEach {
                    mindex = it.child("id").getValue().toString()
                }
                var index = mindex.toInt() + 1
                println("debug user trong add $user")
                ref_user.child(index.toString()).setValue(user)
                show_icon_success_and_move()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun move_to_frag_signing() {
        replacefrag(
            "frag_signing_account",
            frag_signing_account(),
            signup1activity.supportFragmentManager
        )
    }

}