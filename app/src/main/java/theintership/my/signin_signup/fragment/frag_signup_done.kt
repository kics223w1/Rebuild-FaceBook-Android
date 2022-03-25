package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import theintership.my.MainActivity
import theintership.my.MyMethod
import theintership.my.MyMethod.Companion.isWifi
import theintership.my.MyMethod.Companion.replacefrag
import theintership.my.MyMethod.Companion.set_today
import theintership.my.MyMethod.Companion.showToastLong
import theintership.my.R
import theintership.my.databinding.FragSignupDoneBinding
import theintership.my.model.Phone_and_Email_Account
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.shareViewModel
import java.util.*


class frag_signup_done : Fragment(R.layout.frag_signup_done) {

    private var _binding: FragSignupDoneBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private var database: DatabaseReference = Firebase.database.reference
    private val viewmodel: shareViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupDoneBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity


        binding.btnSignupDoneGo.setOnClickListener {
            if (!MyMethod.check_wifi(signup1activity)) {
                return@setOnClickListener
            }
            add_phone_email_account_to_firebase_realtime_database_and_move_frag_create_account()
        }

        binding.btnSignupDoneGo2.setOnClickListener {
            if (!MyMethod.check_wifi(signup1activity)) {
                return@setOnClickListener
            }
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

    private fun move_to_frag_creating_account() {
        replacefrag(
            "frag_signup_creating_account",
            frag_signup_creating_account(),
            signup1activity.supportFragmentManager
        )
    }


    private fun add_phone_email_account_to_firebase_realtime_database_and_move_frag_create_account() {
        if (!isWifi(signup1activity)) {
            val s = "Please connect wifi to continue"
            s.showToastLong(signup1activity)
            return
        }
        val today = set_today()
        println("debug today $today")
        viewmodel.set_user_info_create_at(today)
        var id = 1
        if (viewmodel.index_of_last_ele_phone_email_account != -1) {
            id = viewmodel.index_of_last_ele_phone_email_account + 1
            viewmodel.index_of_last_ele_phone_email_account = id // Update index
            val ss = viewmodel.index_of_last_ele_phone_email_account.toString()
            ss.showToastLong(signup1activity)
        }

        var add_user = false
        var add_phone_email_account = false

        //Add user to ref on firebase realtime database
        viewLifecycleOwner.lifecycleScope.launch{
            withContext(Dispatchers.IO) {
        val ref_user = database.child("User")
        val user = viewmodel.user_info
        val account_ref = viewmodel.account_user
        ref_user.child(account_ref).child("user info").setValue(user)
            .addOnCompleteListener(signup1activity) { task ->
                if (task.isSuccessful) {
                    add_user = true
                    if (add_user && add_phone_email_account) {
                        move_to_frag_creating_account()
                    }
                } else {
                    error_networking_and_move_frag()
                }
            }

        //Add data of phone and email and account on fireabase database
        val ref_phone_email_account = database.child("phone and email and account")
        val email = viewmodel.user_info.email
        val phone = viewmodel.user_info.phone
        val account = viewmodel.account_user
        val phoneAndEmailAccount =
            Phone_and_Email_Account(
                id = id,
                email = email,
                phone = phone,
                account = account
            )
        ref_phone_email_account.child(id.toString()).setValue(phoneAndEmailAccount)
            .addOnCompleteListener(signup1activity) { task ->
                if (task.isSuccessful) {
                    add_phone_email_account = true
                    if (add_user && add_phone_email_account) {
                        move_to_frag_creating_account()
                    }
                } else {
                    error_networking_and_move_frag()
                }
            }
            }
        }
    }

    private fun error_networking_and_move_frag() {
        if (!isWifi(signup1activity)) {
            val s = "Please connect wifi to continue"
            s.showToastLong(signup1activity)
        } else {
            val s = "One thing went wrong , but don't worry just continue"
            s.showToastLong(signup1activity)
            move_to_frag_creating_account()
        }
    }

}