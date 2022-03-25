package theintership.my.signin_signup.fragment

import android.os.Bundle
import android.os.Handler
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
import theintership.my.MyMethod.Companion.isWifi
import theintership.my.MyMethod.Companion.replacefrag
import theintership.my.MyMethod.Companion.showToastLong
import theintership.my.R
import theintership.my.databinding.FragSignupCreatingAccountBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.shareViewModel


class frag_signup_creating_account : Fragment(R.layout.frag_signup_creating_account) {

    private var _binding: FragSignupCreatingAccountBinding? = null
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
        _binding = FragSignupCreatingAccountBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity
        database = Firebase.database.reference
        auth = Firebase.auth
        val account_user = viewmodel.account_user
        val password_user = viewmodel.password_user

        create_auth_user_firebase(account_user, password_user)



        return binding.root
    }

    private fun show_icon_success_and_move() {
//        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        //withContext(Dispatchers.Main) {
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
                    }.withEndAction {
                        move_to_frag_signing()
                    }.start()
                }
            }
        }
//        Handler().postDelayed(
//            { move_to_frag_signing() }, 5000
//        )
        //  }
        //}
    }

    private fun create_auth_user_firebase(account: String, password: String) {
        val email = account + "@gmail.com"
        //Why i set email = account + "@gmail.com"
        //Because when user sign in by method signInWithEmailAndPassword
        //We'll have the account and the password

        //If you see my database realtime structure
        //I store all infomation ( photo , avatar , friends , post , info ) of user
        //according their account
        //So we just need O(1) to get all of that instead of O(n)
        //And when user want to chang their email address
        //We just need find the ref and update it
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            withContext(Dispatchers.IO) {
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
            // delete_user_auth_and_user_info_and_phoneEmailAccount_and_move_frag()
        }
    }

    private fun move_to_frag_signing() {
        replacefrag(
            "frag_signing_account",
            frag_signing(),
            signup1activity.supportFragmentManager
        )
    }


//    private fun delete_user_auth_and_user_info_and_phoneEmailAccount_and_move_frag() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            withContext(Dispatchers.IO) {
//                var delete_user_auth = false
//                var delete_user_info = false
//                var delete_phone_email_account = false
//                var user = Firebase.auth.currentUser
//
//                //Delete user auth
//                user!!.delete().addOnCompleteListener(signup1activity) { task ->
//                    if (task.isSuccessful) {
//                        delete_user_auth = true
//                        if (delete_user_auth && delete_phone_email_account && delete_user_info) {
//                            move_to_frag_signing()
//                        }
//                    }
//                }
//
//                //Deletet user_info
//                val account_ref = viewmodel.account_user
//                val ref_user = database.child("User").child(account_ref).child("user info")
//                ref_user.removeValue().addOnCompleteListener(signup1activity) { task ->
//                    if (task.isSuccessful) {
//                        delete_user_info = true
//                        if (delete_user_auth && delete_phone_email_account && delete_user_info) {
//                            move_to_frag_signing()
//                        }
//                    }
//                }
//
//                // Delete phone and email and account
//                val ref_phoneEmailAccount = database.child("phone and email and account")
//                var id = viewmodel.index_of_last_ele_phone_email_account
//                if (id != -1) {
//                    ref_phoneEmailAccount.child(id.toString()).removeValue()
//                        .addOnCompleteListener(signup1activity) { task ->
//                            if (task.isSuccessful) {
//                                delete_phone_email_account = true
//                                if (delete_user_auth && delete_phone_email_account && delete_user_info) {
//                                    move_to_frag_signing()
//                                }
//                            }
//                        }
//                } else {
//                    delete_phone_email_account = true
//                    if (delete_user_auth && delete_phone_email_account && delete_user_info) {
//                        move_to_frag_signing()
//                    }
//                }
//            }
//        }
//    }

}