package theintership.my.signin_signup.fragment

import android.os.Bundle
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
import kotlinx.coroutines.*
import theintership.my.all_class.MyMethod.Companion.check_wifi
import theintership.my.all_class.MyMethod.Companion.hide_soft_key_board
import theintership.my.all_class.MyMethod.Companion.isWifi
import theintership.my.all_class.MyMethod.Companion.replacefrag
import theintership.my.all_class.MyMethod.Companion.showToastLong
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.R
import theintership.my.databinding.FragAuthEmailAddressAccountBinding
import theintership.my.Signup1Activity
import theintership.my.signin_signup.shareViewModel


class frag_auth_email_address_account : Fragment(R.layout.frag_auth_email_address_account) {

    private var _binding: FragAuthEmailAddressAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private lateinit var auth: FirebaseAuth
    private val shareViewModel: shareViewModel by activityViewModels()
    private lateinit var database: DatabaseReference
    private var is_sending_verification_email_sucess = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragAuthEmailAddressAccountBinding.inflate(inflater, container, false)
        database = Firebase.database.reference
        auth = Firebase.auth
        signup1activity = activity as Signup1Activity

        binding.btnAuthEmailAddressConfirm.setOnClickListener {
            if (!check_wifi(signup1activity)) {
                return@setOnClickListener
            }
            val mUser = Firebase.auth.currentUser
            //Init user again to make sure that isEmailVerified true or false
            //If use the old one , it will not set isEmailVerified on time
            if (mUser != null) {
                mUser.reload()
                if (!mUser.isEmailVerified) {
                    println("debug mUser fail email: ${mUser.email.toString()}")
                    val s = "If you have verified , please click again. Or please verify the eamil."
                    s.showToastShort(requireContext())
                } else {
                    reset_account_user_and_move_frag()
                }
            }
        }


        binding.btnAuthEmailAddressSendAgain.setOnClickListener {
            if (!check_wifi(signup1activity)) {
                return@setOnClickListener
            }
            if (is_sending_verification_email_sucess) {
                val s =
                    "If you don't see email please wait.\nWe use free firebase so it might be late."
                s.showToastLong(signup1activity)
                return@setOnClickListener
            }
            val mUser = Firebase.auth.currentUser
            if (mUser != null) {
                if (!mUser.isEmailVerified) {
                    //Don't need to update email again
                    verify_user_email()
                } else {
                    reset_account_user_and_move_frag()
                }
            }
        }


        binding.btnAuthEmailAddressChangeEmail.setOnClickListener {
            hide_soft_key_board(signup1activity, binding.btnAuthEmailAddressChangeEmail)
            replacefrag(
                "frag_change_email_when_auth",
                frag_change_email_when_auth(),
                signup1activity.supportFragmentManager
            )
        }

        return binding.root
    }

    private fun go_to_frag_set_avatar_and_set_ref_user_info_verify_email() {
        val account_ref = shareViewModel.account_user
        val ref_user_info_verify_email = database
            .child("User")
            .child(account_ref)
            .child("user info")
            .child("verify_email")
        ref_user_info_verify_email.setValue(true)
            .addOnCompleteListener(signup1activity) { task ->
                if (task.isSuccessful) {
                    replacefrag(
                        "frag_set_avatar",
                        frag_set_avatar(),
                        signup1activity.supportFragmentManager
                    )
                } else {
                    error_network()
                }
            }

    }


    private fun reset_account_user_and_move_frag() {
        val user = Firebase.auth.currentUser
        val account_user = shareViewModel.account_user + "@gmail.com"
        println("debug account trong reset: $account_user")
        if (user == null) {
            if (!isWifi(signup1activity)) {
                val s = "Please connect wifi and click send again. Thank you so much."
                s.showToastLong(signup1activity)
            }
            return
        }

        val account_ref = shareViewModel.account_user
        val ref = database
            .child("User")
            .child(account_ref)
            .child("user info")
        shareViewModel.set_user_verify_email(true)

        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                var done_ref = false
                var done_update = false
                ref.setValue(shareViewModel.user_info).addOnCompleteListener(signup1activity) { task ->
                    if (task.isSuccessful) {
                        done_ref = true
                        if (done_ref && done_update) {
                            go_to_frag_set_avatar_and_set_ref_user_info_verify_email()
                        }
                    }else{
                        //We can set it later
                        done_ref = true
                        if (done_ref && done_update) {
                            go_to_frag_set_avatar_and_set_ref_user_info_verify_email()
                        }
                    }
                }

                user.updateEmail(account_user)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            set_tv_email_has_verified()
                            println("debug user email sau khi reset: ${user.email}")
                            done_update = true
                            if (done_ref && done_update) {
                                go_to_frag_set_avatar_and_set_ref_user_info_verify_email()
                            }
                        } else {
                            error_network()
                        }
                    }
                    .addOnFailureListener(signup1activity) {
                        println("debug update email e: $it")
                    }
            }
        }
    }


    private fun verify_user_email() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            if (!user.isEmailVerified) {
                user.sendEmailVerification()
            }
        } else {
            error_network()
        }
    }

    private fun set_tv_email_has_verified() {
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                binding.tvAuthEmailAddressInfo.text =
                    "Your email has been verified. Please click next and go to set your avatar."
                binding.tvAuthEmailAddressStatusEmail.text =
                    "Your email address has been verified."
                binding.tvAuthEmailAddressStatusEmail.setTextColor(
                    resources.getColor(
                        R.color.light_blue,
                        null
                    )
                )

            }
        }
    }

    private fun error_network() {
        if (!isWifi(signup1activity)) {
            val s =
                "Connect wifi is disrupted , pls connect wifi and click send again. Thank you so much."
            s.showToastLong(signup1activity)
            binding.tvAuthEmailAddressInfo.text =
                "Connect wifi is disrupted , pls connect wifi and click send again. Thank you so much."
        } else {
            val s =
                "Please click send again. Some thing went wrong with our sever. Sorry for the error."
            s.showToastLong(signup1activity)
            binding.tvAuthEmailAddressInfo.text =
                "Please click send again. Some thing went wrong with our sever. Sorry for the error."
        }
    }

}