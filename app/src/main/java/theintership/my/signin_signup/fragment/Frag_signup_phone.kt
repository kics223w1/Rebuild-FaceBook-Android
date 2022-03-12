package theintership.my.signin_signup.fragment

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.MainActivity
import theintership.my.MyMethod
import theintership.my.MyMethod.Companion.replacefrag
import theintership.my.MyMethod.Companion.showToastLong
import theintership.my.R
import theintership.my.databinding.FragSignupPhoneBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.viewModel_Signin_Signup
import kotlin.math.sign

class frag_signup_phone : Fragment(R.layout.frag_signup_phone) {

    private var _binding: FragSignupPhoneBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity
    private val viewModel_Signin_Signup: viewModel_Signin_Signup by activityViewModels()
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupPhoneBinding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity
        database = Firebase.database.reference
        //signup1Activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        signup1Activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        binding.btnSignupPhoneGo.setOnClickListener {
            val phone = binding.edtSignupPhone.text.toString()
            val country_code = binding.edtSignupCountryCode.text.toString()
            if (phone.length < 6) {
                set_error_edittext("Please enter valid phone number , like VietNamese phone number or you can use email address.")
                return@setOnClickListener
            }
            if (!check_country_code(country_code)) {
                set_error_edittext("Please just enter valid country code\\n Please enter number in country code and don't enter '+' or charaters.")
                return@setOnClickListener
            }
            val list_phone_number = viewModel_Signin_Signup.list_phone_number
            if (list_phone_number.contains(phone)) {
                set_error_edittext("Phone number already use by another user")
                return@setOnClickListener
            }
            move_error_edittext()
            val fm = signup1Activity.supportFragmentManager
            val size = fm.backStackEntryCount
            if (fm.getBackStackEntryAt(size - 2).name == "frag_signup_phone") {
                //This condition will come up
                // when user want to change phone number in frag_auth_phone_number_account
                go_to_frag_auth_phone_number_account(phone, country_code)
            } else {
                goto_frag_signup_email(phone, country_code)
            }
        }

        binding.edtSignupCountryCode.setOnEditorActionListener { textView, i, keyEvent ->
            val phone = binding.edtSignupPhone.text.toString()
            val country_code = binding.edtSignupCountryCode.text.toString()
            val list_phone_number = viewModel_Signin_Signup.list_phone_number
            if (phone.length < 6) {
                set_error_edittext("Please enter valid phone number , like VietNamese phone number or you can use email address.")
                false
            } else if (list_phone_number.contains(phone)) {
                set_error_edittext("Phone number already use by another user")
                false
            } else if (!check_country_code(country_code)) {
                set_error_edittext("Please just enter valid country code\n Please enter number in country code and don't enter '+' or charaters.")
                false
            } else {
                move_error_edittext()
                val fm = signup1Activity.supportFragmentManager
                val size = fm.backStackEntryCount
                if (fm.getBackStackEntryAt(size - 2).name == "frag_signup_phone") {
                    //This condition will come up
                    // when user want to change phone number in frag_auth_phone_number_account
                    go_to_frag_auth_phone_number_account(phone, country_code)
                } else {
                    goto_frag_signup_email(phone, country_code)
                }
                true
            }
        }

        binding.btnSignupPhoneEmailAddress.setOnClickListener {
            goto_frag_signup_email("", "")
        }



        binding.btnSignupPhoneBack.setOnClickListener {
            val dialog = dialog_stop_signup(signup1Activity)
            dialog.show()
            dialog.btn_cancel.setOnClickListener {
                startActivity(Intent(signup1Activity, MainActivity::class.java))
                signup1Activity.overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                dialog.dismiss()
            }
        }

        return binding.root
    }

    private fun check_country_code(country_code: String): Boolean {
        if (country_code == "") {
            //User want to use the defaut VietNamese country code
            return true
        }
        if (country_code.length > 3 || country_code.length < 2) {
            return false
        }
        country_code.forEach {
            if (it !in '0'..'9') {
                return false
            }
        }
        return true
    }

    fun set_error_edittext(str: String) {
        binding.tvSignupPhoneInfo.text = str
        binding.tvSignupPhoneInfo.setTextColor(resources.getColor(R.color.error, null))
    }

    fun move_error_edittext() {
        binding.tvSignupPhoneInfo.text =
            "Please enter your phone number. You can always make this private later"
        binding.tvSignupPhoneInfo.setTextColor(resources.getColor(R.color.light_grey, null))
    }

    fun goto_frag_signup_email(phone: String, country_code: String) {
        replacefrag(
            "frag_signup_email",
            frag_signup_email(),
            signup1Activity.supportFragmentManager
        )
        viewModel_Signin_Signup.set_user_info_phone(phone)
        if (country_code != "") {
            viewModel_Signin_Signup.user_info.country_code = country_code
        }
    }

    fun go_to_frag_auth_phone_number_account(phone_number: String, country_code: String) {
        viewModel_Signin_Signup.set_user_info_phone(phone_number)
        viewModel_Signin_Signup.set_user_info_country_code(country_code)

        val account_ref = viewModel_Signin_Signup.account_user

        val ref_user_info_phone_number = database
            .child("User")
            .child(account_ref)
            .child("phone")

        val ref_user_info_country_code = database
            .child("User")
            .child(account_ref)
            .child("country_code")

        var done_phone = false
        var done_country_code = false
        ref_user_info_phone_number.setValue(phone_number)
            .addOnCompleteListener(signup1Activity) { task ->
                if (task.isSuccessful) {
                    done_phone = true
                    if (done_phone && done_country_code) {
                        //We must make sure that our database is updated and then popBackStack
                        signup1Activity.supportFragmentManager.popBackStack()
                    }
                }else{
                    error_networking_and_move_frag()
                }
            }
        if (country_code != "") {
            //Defaut country code is 84 so if user don't enter country code ,
            //so we leave it is 84
            ref_user_info_country_code.setValue(country_code)
                .addOnCompleteListener(signup1Activity) { task ->
                    if (task.isSuccessful) {
                        done_country_code = true
                        if (done_phone && done_country_code) {
                            //We must make sure that our database is updated and then popBackStack
                            signup1Activity.supportFragmentManager.popBackStack()
                        }
                    } else {
                       error_networking_and_move_frag()
                    }
                }
        } else {
            done_country_code = true
            if (done_phone && done_country_code) {
                signup1Activity.supportFragmentManager.popBackStack()
            }
        }
    }


    private fun error_networking_and_move_frag() {
        if (!MyMethod.isWifi(signup1Activity)) {
            val s = "Please connect wifi to continue"
            s.showToastLong(signup1Activity)
        } else {
            val s  = "One thing went wrong , but don't worry just continue"
            s.showToastLong(signup1Activity)
            signup1Activity.supportFragmentManager.popBackStack()
        }
    }}