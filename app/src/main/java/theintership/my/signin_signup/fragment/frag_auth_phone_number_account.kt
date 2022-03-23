package theintership.my.signin_signup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import theintership.my.MyMethod.Companion.check_wifi
import theintership.my.MyMethod.Companion.hide_soft_key_board
import theintership.my.MyMethod.Companion.isWifi
import theintership.my.MyMethod.Companion.replacefrag
import theintership.my.MyMethod.Companion.replacefrag_by_silde_in_left
import theintership.my.MyMethod.Companion.showToastLong
import theintership.my.MyMethod.Companion.showToastShort
import theintership.my.R
import theintership.my.databinding.FragAuthPhoneNumberAccountBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.shareViewModel
import java.util.concurrent.TimeUnit


class frag_auth_phone_number_account : Fragment(R.layout.frag_auth_phone_number_account) {

    private var _binding: FragAuthPhoneNumberAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private lateinit var auth: FirebaseAuth
    private val shareViewmodel: shareViewModel by activityViewModels()
    private lateinit var database: DatabaseReference
    private lateinit var mForceResendingToken: PhoneAuthProvider.ForceResendingToken
    private var verify_false_first_time = false
    private var verify_false_again = false
    private var verifyID = ""
    private var list_mForceResendingToken = mutableListOf<String>()
    private var list_mForceResendingToken_is_use = mutableListOf<Boolean>()
    private var number_of_auth_phone_in_a_day = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragAuthPhoneNumberAccountBinding.inflate(inflater, container, false)
        database = Firebase.database.reference
        signup1activity = activity as Signup1Activity
        auth = Firebase.auth
        auth.setLanguageCode("en")
        number_of_auth_phone_in_a_day = shareViewmodel.number_of_auth_phone_number_in_a_day

        println("debug number dau tien ne: $number_of_auth_phone_in_a_day")

        println("debug auth email first time: ${shareViewmodel.first_time_auth_email_address}")

        if (shareViewmodel.first_time_auth_phone_number || shareViewmodel.is_phone_number_change) {
            if (shareViewmodel.first_time_auth_phone_number) {
                shareViewmodel.first_time_auth_phone_number = false
            }
            if (shareViewmodel.is_phone_number_change) {
                shareViewmodel.is_phone_number_change = false
            }
            set_tv_before_sending_SMS_code()
            verify_first_time()
        } else {
            set_tv_after_sending_SMS_code()
        }


        binding.btnAuthPhoneNumberConfirm.setOnClickListener {
            if (!check_wifi(signup1activity)) {
                return@setOnClickListener
            }
            val otp = binding.edtAuthPhoneNumberAccount.text.toString()
            if (otp == "") {
                val s = "Please enter otp code that you received"
                s.showToastShort(signup1activity)
                return@setOnClickListener
            }
            if (verifyID != "") {
                val credential = PhoneAuthProvider.getCredential(verifyID, otp)
                signInWithPhoneAuthCredential(credential)
            }
        }




        binding.edtAuthPhoneNumberAccount.setOnEditorActionListener { textview, i, keyevent ->
            val otp = binding.edtAuthPhoneNumberAccount.text.toString()
            if (otp == "") {
                val s = "Please enter otp code that you received"
                s.showToastShort(signup1activity)
                false
            } else if (!check_wifi(signup1activity)) {
                false
            } else if (verifyID != "") {
                val credential = PhoneAuthProvider.getCredential(verifyID, otp)
                signInWithPhoneAuthCredential(credential)
                true
            } else {
                false
            }
        }

        binding.btnAuthPhoneNumberConfirmByEmail.setOnClickListener {
            val email = shareViewmodel.user_info.email.toString()
            if (email == "") {
                val s = "You didn't enter email address , so you can't not authencation by it."
                s.showToastLong(signup1activity)
                return@setOnClickListener
            }
            replacefrag(
                "frag_auth_email_address_account",
                frag_auth_email_address_account(),
                signup1activity.supportFragmentManager
            )
        }

        binding.btnAuthPhoneNumberChangePhoneNumber.setOnClickListener {
            hide_soft_key_board(signup1activity, binding.btnAuthPhoneNumberChangePhoneNumber)
            replacefrag(
                "frag_change_phone_when_auth",
                frag_change_phone_when_auth(),
                signup1activity.supportFragmentManager
            )
        }


        binding.btnAuthPhoneNumberSendAgain.setOnClickListener {
            if (!check_wifi(signup1activity)) {
                return@setOnClickListener
            }
            println("debug number of auth trong send again: $number_of_auth_phone_in_a_day")
            if (number_of_auth_phone_in_a_day >= 10) {
                set_tv_reach_the_limit_of_number_authencation_phone()
                delete_user_auth_and_user_info_and_phoneEmailAccount()
                return@setOnClickListener
            }
            if (verify_false_first_time && !verify_false_again) {
                verify_first_time()
                return@setOnClickListener
            }
            if (verify_false_first_time && verify_false_again) {
                set_tv_reach_the_limit_of_number_authencation_phone()
                delete_user_auth_and_user_info_and_phoneEmailAccount()
                return@setOnClickListener
            }
            val size = list_mForceResendingToken_is_use.size
            if (list_mForceResendingToken_is_use[size - 1]) {
                set_tv_reach_the_limit_of_number_authencation_phone()
                delete_user_auth_and_user_info_and_phoneEmailAccount()
                return@setOnClickListener
            }
            //User receive verification email but want to send again , so we call verify_again()
            //Add the token we got from verify_first_time and we use it for resend email
            list_mForceResendingToken.add(mForceResendingToken.toString())
            list_mForceResendingToken_is_use.add(true)
            verify_again()
        }

        return binding.root
    }


    private fun set_phone_number_for_send_otp(): String {
        var phone_number = ""
        //Phone number for opt must has format "+" and "country code" and "
        val country_code = shareViewmodel.user_info.country_code
        var phone_user = shareViewmodel.user_info.phone.toString()
        if (phone_user[0] == '0' && country_code == "84") {
            //VietNamese phone
            val temp = phone_user
            phone_user = ""
            for (i in 1 until temp.length) {
                phone_user += temp[i]
            }
        }
        phone_number = "+" + country_code + phone_user

        return phone_number
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(signup1activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val s = "Verify success"
                    s.showToastShort(signup1activity)
                    set_ref_verify_phone_and_move_frag()
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        val s = "The verification code entered was invalid or experid"
                        s.showToastShort(signup1activity)
                    } else {
                        val s = "Some thing wrong with our sever. Please click send again."
                        s.showToastLong(signup1activity)
                    }
                }
            }

    }

    private fun set_ref_verify_phone_and_move_frag() {
        val account_ref = shareViewmodel.account_user
        val user = Firebase.auth.currentUser!!
        var done_ref = false
        var done_delete = false
        //Delete this user , because we have user signInWithEmailAndPassword
        //so we don't need the user SignInWithPhoneAuthCredential
        user.delete().addOnCompleteListener(signup1activity) { task ->
            if (task.isSuccessful) {
                done_delete = true
                if (done_delete && done_ref) {
                    go_to_frag_set_avatar()
                }
            }
        }

        //Set verify phone = true in firebase realtime database
        val ref_verify_phone = database
            .child("User")
            .child(account_ref)
            .child("user info")
            .child("verify_phone")
        ref_verify_phone.setValue(true).addOnCompleteListener(signup1activity) { task ->
            if (task.isSuccessful) {
                done_ref = true
                if (done_ref && done_delete) {
                    go_to_frag_set_avatar()
                }
            }
        }
    }

    private fun go_to_frag_set_avatar() {
        replacefrag(
            "frag_set_avatar",
            frag_set_avatar(),
            signup1activity.supportFragmentManager
        )
    }


    private fun verify_first_time() {
        //If the program enter this fragment , this phoneNumber is non-empty
        //See the condition in frag_signup_creating_account
        //This Verificaiton is verify of you are not a robot , not verify the opt code
        //onCodeSent will be invoked when the SMS sent to user phone

        val phoneNumber: String = set_phone_number_for_send_otp()
        println("debug phone number trong verify first time: $phoneNumber")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(100L, TimeUnit.SECONDS)
            .setActivity(signup1activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    val s = "Verification Completed"
                    s.showToastShort(signup1activity)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    if (verify_false_first_time) {
                        verify_false_again = true
                    } else {
                        verify_false_first_time = true
                    }
                    val s = "Verification fail. Please click send again."
                    s.showToastShort(signup1activity)
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    set_tv_after_sending_SMS_code()
                    verifyID = p0
                    mForceResendingToken = p1
                    list_mForceResendingToken.add(mForceResendingToken.toString())
                }

            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun verify_again() {
        //The program just coming this case when mFroceResendingToken != ""
        //You can make it clear by seeing code in binding.btnAuthPhoneNumberAgain
        val phoneNumber: String = set_phone_number_for_send_otp()
        println("debug phone number trong verify first time: $phoneNumber")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(100L, TimeUnit.SECONDS)
            .setActivity(signup1activity)
            .setForceResendingToken(mForceResendingToken)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    val s = "Verification Completed"
                    s.showToastShort(signup1activity)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    val s = "Verification fail. Please click send again."
                    s.showToastShort(signup1activity)
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    set_tv_after_sending_SMS_code()
                    verifyID = p0
                    mForceResendingToken = p1
                    //Add the new token we just receive and not use
                    list_mForceResendingToken.add(mForceResendingToken.toString())
                    list_mForceResendingToken_is_use.add(false)

                    //Set the token before is use
                    val size = list_mForceResendingToken_is_use.size
                    list_mForceResendingToken_is_use[size - 2] = true
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)


    }

    private fun set_tv_after_sending_SMS_code() {

        val ref_limit_auth_phone = database
            .child("limit_auth_phone_in_a_day")
            .child("1")
            .child("number")
        number_of_auth_phone_in_a_day += 1
        ref_limit_auth_phone.setValue(number_of_auth_phone_in_a_day.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("debug number of auth trong task: $number_of_auth_phone_in_a_day")
                    binding.tvFragAuthPhoneNumberInfo.text = "Enter the code that we've sent to " +
                            "${shareViewmodel.user_info.phone} in your SMS"
                    binding.tvFragAuthPhoneNumberStatus.visibility = View.VISIBLE
                } else {
                    println("debug number of auth trong task: $number_of_auth_phone_in_a_day")
                    //It's same ,I know but it's might be not too important.
                    //So just let it here
                    binding.tvFragAuthPhoneNumberInfo.text = "Enter the code that we've sent to " +
                            "${shareViewmodel.user_info.phone} in your SMS"
                    binding.tvFragAuthPhoneNumberStatus.visibility = View.VISIBLE
                }
            }

    }

    private fun set_tv_before_sending_SMS_code() {
        binding.tvFragAuthPhoneNumberStatus.visibility = View.GONE
        binding.tvFragAuthPhoneNumberInfo.text =
            "Please wait for us to send you the SMS code to verify phone number"
    }

    private fun set_tv_reach_the_limit_of_number_authencation_phone() {
        val s = "You just reach the limit of number authencation phone in a day.\n" +
                "We'll delete all infomation you were typing and then you can sign up again in tomorrow.\n" +
                "Thanks for signing up this project.\n" +
                "We use free Firebase for backend so it has limit of authencation in a day."
        binding.tvFragAuthPhoneNumberInfo.setText(s)
        binding.tvFragAuthPhoneNumberInfo.setTextColor(resources.getColor(R.color.light_blue, null))
        s.showToastLong(signup1activity)
    }


    private fun delete_user_auth_and_user_info_and_phoneEmailAccount() {
        var delete_user_auth = false
        var delete_phone_email_account = false
        var user = Firebase.auth.currentUser
        shareViewmodel.is_delete_user = true

        var id = shareViewmodel.index_of_last_ele_phone_email_account
        val ref_phoneEmailAccount = database.child("phone and email and account")
        var idd = id

        val s = "Delete user success"

        //Need to delet user_auth , user_info , user_phone_email_account

        //But user_info and user_phone_email_account are stored in firebase realtime database
        //And this fragment will crash when we delete them in different time or same time
        //So i must move function delete user_info to onDestroy in Signup1Actiivity

        //In general , if user_auth and user_phone email account are still in firebase realtime
        //other people will not be able create account because the infomation is duplicated with
        //non-exitsiting user

        //so delete them is priority in this funciton
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                //Delete user auth
                if (user != null) {
                    user.delete().addOnCompleteListener(signup1activity) { task ->
                        if (task.isSuccessful) {
                            delete_user_auth = true
                            if (delete_user_auth && delete_phone_email_account ) {
                                s.showToastLong(signup1activity)
                            }
                        }
                    }
                } else {
                    delete_user_auth = true
                    if (delete_user_auth && delete_phone_email_account ) {
                        s.showToastLong(signup1activity)
                    }
                }

                //Delete phone and email and account
                if (idd != -1) {
                    ref_phoneEmailAccount.child(idd.toString()).removeValue()
                        .addOnCompleteListener(signup1activity) { task ->
                            if (task.isSuccessful) {
                                delete_phone_email_account = true
                                if (delete_user_auth && delete_phone_email_account ) {
                                    s.showToastLong(signup1activity)
                                }
                            }
                        }
                } else {
                    delete_phone_email_account = true
                    if (delete_user_auth && delete_phone_email_account ) {
                        s.showToastLong(signup1activity)
                    }
                }

                //We will continue delete user_info in onDestroy in Signup1Activity
            }
        }
    }
}

