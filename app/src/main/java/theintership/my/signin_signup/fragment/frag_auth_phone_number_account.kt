package theintership.my.signin_signup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.MyMethod.Companion.replacefrag
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
    private val viewmodel: shareViewModel by activityViewModels()
    private lateinit var mForceResendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var database: DatabaseReference
    private var verifyID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        signup1activity = activity as Signup1Activity
        auth = Firebase.auth
        auth.setLanguageCode("en")

        //If the program enter this fragment , this phoneNumber is non-empty
        //See the condition in frag_signup_creating_account
        val phoneNumber: String = set_phone_number_for_send_otp()

        //This Verificaiton is verify of you are not a robot , not verify the opt code
        //onCodeSent will be invoked when the SMS sent to user phone
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
                    val s = "Verification Fail , Please click send again"
                    s.showToastLong(signup1activity)
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    verifyID = p0
                    mForceResendingToken = p1
                }

            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragAuthPhoneNumberAccountBinding.inflate(inflater, container, false)
        database = Firebase.database.reference

        binding.tvFragAuthPhoneNumberInfo.text = "Enter the code that we've sent to " +
                "${viewmodel.user_info.phone} in your SMS"

        binding.btnAuthPhoneNumberConfirm.setOnClickListener {
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
            } else if (verifyID != "") {
                val credential = PhoneAuthProvider.getCredential(verifyID, otp)
                signInWithPhoneAuthCredential(credential)
                true
            } else {
                false
            }
        }

        binding.btnAuthPhoneNumberConfirmByEmail.setOnClickListener {
            replacefrag(
                "frag_auth_email_address_account",
                frag_auth_email_address_account(),
                signup1activity.supportFragmentManager
            )
        }

        binding.btnAuthPhoneNumberChangePhoneNumber.setOnClickListener {
            viewmodel.is_user_change_phone_when_authencation = true
            replacefrag(
                "frag_signup_phone",
                frag_signup_phone(),
                signup1activity.supportFragmentManager
            )
        }


        binding.btnAuthPhoneNumberSendAgain.setOnClickListener {
            val phoneNumber: String = set_phone_number_for_send_otp()

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(100L, TimeUnit.SECONDS)
                .setActivity(signup1activity)
                .setForceResendingToken(mForceResendingToken)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        val s = "Verification Completed"
                        s.showToastShort(signup1activity)
                        signInWithPhoneAuthCredential(p0)
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        val s = "Verification Fail , Please click send again"
                        s.showToastLong(signup1activity)
                    }

                    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        super.onCodeSent(p0, p1)
                        verifyID = p0
                        mForceResendingToken = p1
                    }

                })
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }


        return binding.root
    }


    private fun set_phone_number_for_send_otp(): String {
        var phone_number = ""
        //Phone number for opt must has format "+" and "country code" and "
        val country_code = viewmodel.user_info.country_code
        var phone_user = viewmodel.user_info.phone.toString()
        if (phone_user[0] == '0' && country_code == "84") {
            //VietNamese phone
            phone_user.drop(1)
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
                    Firebase.auth.signOut()
                    // Sign out because we already sign in by
                    // SignInWithEmailAndPassword in frag_signing
                    set_ref_verify_phone_and_move_frag()
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        val s = "The verification code entered was invalid"
                        s.showToastShort(signup1activity)
                    } else {
                        val s = "Some thing wrong with our sever. Please click send again."
                        s.showToastLong(signup1activity)
                    }
                    // Update UI
                }
            }
    }

    private fun set_ref_verify_phone_and_move_frag() {
        val account_ref = viewmodel.account_user
        val ref_verify_phone = database
            .child("User")
            .child(account_ref)
            .child("user info")
            .child("verify_phone")
        ref_verify_phone.setValue(true).addOnCompleteListener(signup1activity) { task ->
            if (task.isSuccessful) {
                replacefrag(
                    "frag_set_avatar",
                    frag_set_avatar(),
                    signup1activity.supportFragmentManager
                )
            }
        }
    }
}