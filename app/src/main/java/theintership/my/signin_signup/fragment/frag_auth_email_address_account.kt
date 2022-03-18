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
import kotlinx.coroutines.*
import theintership.my.MyMethod.Companion.hide_soft_key_board
import theintership.my.MyMethod.Companion.isWifi
import theintership.my.MyMethod.Companion.replacefrag
import theintership.my.MyMethod.Companion.replacefrag_by_silde_in_left
import theintership.my.MyMethod.Companion.showToastLong
import theintership.my.MyMethod.Companion.showToastShort
import theintership.my.R
import theintership.my.databinding.FragAuthEmailAddressAccountBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.shareViewModel


class frag_auth_email_address_account : Fragment(R.layout.frag_auth_email_address_account) {

    private var _binding: FragAuthEmailAddressAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private lateinit var auth: FirebaseAuth
    private val shareViewModel: shareViewModel by activityViewModels()
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragAuthEmailAddressAccountBinding.inflate(inflater, container, false)
        database = Firebase.database.reference
        auth = Firebase.auth
        signup1activity = activity as Signup1Activity
        auth.setLanguageCode("en")

        if (shareViewModel.first_time_auth_email_address || shareViewModel.is_email_address_change) {
            if (shareViewModel.first_time_auth_email_address) {
                shareViewModel.first_time_auth_email_address = false
            }
            if (shareViewModel.is_email_address_change) {
                shareViewModel.is_email_address_change = false
            }
            val User = Firebase.auth.currentUser
            if (User != null) {
                if (!User.isEmailVerified) {
                    update_email_user_and_verify_it()
                }
            }
        } else {
            binding.tvAuthEmailAddressInfo.text =
                "Please check the verification email which we've just sent you"
            val s = "Please check your email to verify."
            s.showToastLong(signup1activity)
        }


        binding.btnAuthEmailAddressConfirm.setOnClickListener {
            val mUser = Firebase.auth.currentUser
            //Init user again to make sure that isEmailVerified true or false
            //If use the old one , it will not set isEmailVerified on time
            if (mUser != null) {
                val ok = mUser.isEmailVerified
                if (!ok) {
                    val s = "Please click again to confirm verification."
                    s.showToastLong(signup1activity)
                    update_email_user_and_verify_it()
                } else {
                    reset_account_user_and_move_frag()
                }
            }
        }


        binding.btnAuthEmailAddressSendAgain.setOnClickListener {
            val mUser = Firebase.auth.currentUser
            //Init user again to make sure that isEmailVerified true or false
            //If use the old one , it will not set isEmailVerified on time
            if (mUser != null) {
                if (!mUser.isEmailVerified) {
                    update_email_user_and_verify_it()
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

        binding.btnAuthEmailAddressConfirmByPhoneNumber.setOnClickListener {
            val phone = shareViewModel.user_info.phone
            if (phone == "") {
                val s =
                    "We can't confirm phone number when you don't enter it when sign up account."
                s.showToastLong(signup1activity)
                return@setOnClickListener
            } else {
                signup1activity.supportFragmentManager.popBackStack()
            }
        }



        return binding.root
    }

    private fun go_to_frag_set_avatar_and_set_ref_user_info_verify_email() {
        runBlocking {
            var done = false
            val job = CoroutineScope(Dispatchers.IO).launch {
                val account_ref = shareViewModel.account_user
                val ref_user_info_verify_email = database
                    .child("User")
                    .child(account_ref)
                    .child("user info")
                    .child("verify_email")
                ref_user_info_verify_email.setValue(true)
                    .addOnCompleteListener(signup1activity) { task ->
                        if (task.isSuccessful) {
                            done = true
                            replacefrag(
                                "frag_set_avatar",
                                frag_set_avatar(),
                                signup1activity.supportFragmentManager
                            )
                        } else {
                            error_network()
                            done = true
                        }
                    }
            }
            if (done) {
                job.cancel()
            }
        }

    }


    private fun update_email_user_and_verify_it() {
        //My method for sign in in this app is SignInWithEmailAndPassword
        //You can see it in frag_signing
        //And you will see that i set email = account + "@gmail.com"
        //I have explained why i did that in frag_signing in function signin_user_and_move_frag

        //So firebase will understand that email of user is "user.account + @gmail.com"
        //So when we need to send a verification email to user
        //We need to update email of user to user.email that user has entered
        //When we done we just need reset it to the way it was

        val user = Firebase.auth.currentUser
        val email_address_for_verification = shareViewModel.user_info.email.toString()
        println("debug vao update email ne $email_address_for_verification")
        if (user == null) {
            if (!isWifi(signup1activity)) {
                val s = "Please connect wifi and click send again. Thank you so much."
                s.showToastLong(signup1activity)
            }
            return
        }
        runBlocking {
            var done = false
            val job = CoroutineScope(Dispatchers.IO).launch {
                user.updateEmail(email_address_for_verification)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            done = true
                            verify_user_email()
                        } else {
                            done = true
                            println("debug vao error network trong update email")
                            error_network()
                        }
                    }
                    .addOnFailureListener(signup1activity) {
                        done = true
                        println("debug update email e: $it")
                    }
            }
            if (done) {
                job.cancel()
            }
        }


    }


    private fun reset_account_user_and_move_frag() {
        println("debug vao reset email ne")
        val user = Firebase.auth.currentUser
        val account_user = shareViewModel.account_user + "@gmail.com"
        if (user == null) {
            if (!isWifi(signup1activity)) {
                val s = "Please connect wifi and click send again. Thank you so much."
                s.showToastLong(signup1activity)
            }
            return
        }
        runBlocking {
            var done = false
            val job = CoroutineScope(Dispatchers.IO).launch {
                user.updateEmail(account_user)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            done = true
                            binding.tvAuthEmailAddressStatusEmail.text =
                                "Your email address has been verified"
                            binding.tvAuthEmailAddressStatusEmail.setTextColor(
                                resources.getColor(
                                    R.color.light_blue,
                                    null
                                )
                            )
                            println("debug user email sau khi reset: ${user.email}")
                            go_to_frag_set_avatar_and_set_ref_user_info_verify_email()
                        } else {
                            done = true
                            error_network()
                        }
                    }
                    .addOnFailureListener(signup1activity) {
                        done = true
                        println("debug update email e: $it")
                    }
            }
            if (done) {
                job.cancel()
            }
        }

    }

    private fun verify_user_email() {
        val email_user = shareViewModel.user_info.email
        val user = Firebase.auth.currentUser
        runBlocking {
            var done = false

            val job = CoroutineScope(Dispatchers.IO).launch {
                if (user != null) {
                    println("debug user email trong verify: ${user.email}")
                    if (!user.isEmailVerified) {
                        withContext(Dispatchers.Main) {
                            binding.tvAuthEmailAddressInfo.text =
                                "We are sending a verification email to ${email_user}"
                        }
                        user.sendEmailVerification()
                            .addOnCompleteListener(signup1activity) { task ->
                                if (task.isSuccessful) {
                                    done = true
                                    set_tv_after_sending_verification_email()
                                    val s = "Please check your email to verify."
                                    s.showToastLong(signup1activity)
                                } else {
                                    done = true
                                    error_network()
                                }
                            }
                    } else {
                        done = true
                        set_tv_email_has_verified()
                    }
                } else {
                    done = true
                    error_network()
                }
            }
            if (done) {
                job.cancel()
            }
        }


    }

    private fun set_tv_email_has_verified() {
        runBlocking {
            val job = CoroutineScope(Dispatchers.Main).launch {
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
            job.join()
            job.cancel()
        }
    }

    private fun set_tv_after_sending_verification_email() {
        runBlocking {
            val job = CoroutineScope(Dispatchers.Main).launch {
                binding.tvAuthEmailAddressInfo.text =
                    "Please check the verification email which we've just sent you"
            }
            job.join()
            job.cancel()
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