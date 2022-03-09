package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
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
import theintership.my.`interface`.ICheckWifi
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSignupDoneBinding
import theintership.my.model.Phone_and_Email_Account
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.viewModel_Signin_Signup
import java.util.*
import kotlin.math.sign


class frag_signup_done : Fragment(R.layout.frag_signup_done), IReplaceFrag, IToast, ICheckWifi {

    private var _binding: FragSignupDoneBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private var database: DatabaseReference = Firebase.database.reference
    private val viewmodelSigninSignup: viewModel_Signin_Signup by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupDoneBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity


        binding.btnSignupDoneGo.setOnClickListener {
            add_phone_email_account_to_firebase_realtime_database_and_move_frag_create_account()
        }

        binding.btnSignupDoneGo2.setOnClickListener {
            add_phone_email_account_to_firebase_realtime_database_and_move_frag_create_account()
        }


        binding.btnSignupDoneBack.setOnClickListener {
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

    private fun move_to_frag_create_account() {
        replacefrag(
            "frag_signup_creating_account",
            frag_signup_creating_account(),
            signup1activity.supportFragmentManager
        )
    }

    private fun add_phone_email_account_to_firebase_realtime_database_and_move_frag_create_account() {
        if (!isWifi(signup1activity)) {
            showLong("Please connect wifi to continue", signup1activity)
            return
        }
        var today = Calendar.getInstance().toString()
        viewmodelSigninSignup.set_user_info_create_at(today)
        println("debug vao add phone ne")
        var id = 1
        val ref_phone_email_account = database.child("phone and email and account")
        ref_phone_email_account.orderByKey().limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val element = snapshot.children
                    element.forEach {
                        //Take the last id
                        val mid = it.child("id").getValue().toString()
                        id += mid.toInt()
                    }
                    add_phone_email_account_to_firebase_and_move(id = id)
                }


                override fun onCancelled(error: DatabaseError) {
                    if (!isWifi(signup1activity)) {
                        showLong("Please connect wifi to continue", signup1activity)
                    } else {
                        show(
                            "One thing went wrong , but don't worry just continue",
                            signup1activity
                        )
                        move_to_frag_create_account()
                    }
                }

            })

        //Just come here when ref_phone_email_account hasn't init on firebase realtime database
        add_phone_email_account_to_firebase_and_move(id = id)
    }

    private fun add_phone_email_account_to_firebase_and_move(id :Int) {
        val ref_phone_email_account = database.child("phone and email and account")
        val email = viewmodelSigninSignup.user_info.email
        val phone = viewmodelSigninSignup.user_info.phone
        val phoneAndEmailAccount =
            Phone_and_Email_Account(id = id, email = email, phone = phone)
        ref_phone_email_account.child(id.toString()).setValue(phoneAndEmailAccount)
            .addOnCompleteListener(signup1activity) { task ->
                if (task.isSuccessful) {
                    println("debug vao move ne $id")
                    move_to_frag_create_account()
                } else {
                    if (!isWifi(signup1activity)) {
                        showLong("Please connect wifi to continue", signup1activity)
                    } else {
                        show(
                            "One thing went wrong , but don't worry just continue",
                            signup1activity
                        )
                        move_to_frag_create_account()
                    }
                }
            }
        println("debug qua khuc move roi")
    }


}