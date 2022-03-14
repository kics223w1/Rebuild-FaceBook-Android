package theintership.my.signin_signup.fragment

import android.content.Intent
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
import theintership.my.MyMethod.Companion.hide_soft_key_board
import theintership.my.MyMethod.Companion.replacefrag
import theintership.my.MyMethod.Companion.showToastLong
import theintership.my.R
import theintership.my.databinding.FragSignupPhoneBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.shareViewModel

class frag_signup_phone : Fragment(R.layout.frag_signup_phone) {

    private var _binding: FragSignupPhoneBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity
    private val shareViewModel: shareViewModel by activityViewModels()
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
        val user_change_phone_when_authencation = shareViewModel.is_user_change_phone_when_authencation


        if (user_change_phone_when_authencation) {
            //Chang some UI to make user easy to understand
            binding.btnSignupPhoneEmailAddress.visibility = View.GONE
            binding.tvSignupPhoneInfo.text = "Please enter new phone number you want to change."
        }


        binding.btnSignupPhoneGo.setOnClickListener {
            val phone = binding.edtSignupPhone.text.toString()
            val country_code = binding.edtSignupCountryCode.text.toString()
            hide_soft_key_board(signup1Activity, binding.btnSignupPhoneGo)
            if (!validd_phone_number_and_country_code(phone = phone, country_code = country_code)) {
                return@setOnClickListener
            }

            move_error_edittext()
            if (user_change_phone_when_authencation) {
                go_to_frag_auth_phone_number_account(
                    phone_number = phone,
                    country_code = country_code
                )
            } else {
                goto_frag_signup_email(
                    phone = phone,
                    country_code = country_code
                )
            }
        }

        binding.edtSignupCountryCode.setOnEditorActionListener { textView, i, keyEvent ->
            val phone = binding.edtSignupPhone.text.toString()
            val country_code = binding.edtSignupCountryCode.text.toString()
            hide_soft_key_board(signup1Activity, binding.edtSignupCountryCode)
            if (validd_phone_number_and_country_code(phone = phone, country_code = country_code)) {
                move_error_edittext()
                if (user_change_phone_when_authencation) {
                    go_to_frag_auth_phone_number_account(
                        phone_number = phone,
                        country_code = country_code
                    )
                    true
                } else {
                    goto_frag_signup_email(
                        phone = phone,
                        country_code = country_code
                    )
                    true
                }
            } else {
                false
            }


        }

        binding.btnSignupPhoneEmailAddress.setOnClickListener {
            goto_frag_signup_email(
                phone = "",
                country_code = ""
            )
        }



        binding.btnSignupPhoneBack.setOnClickListener {
            hide_soft_key_board(signup1Activity, binding.btnSignupPhoneBack)
            if (user_change_phone_when_authencation){
                shareViewModel.is_user_change_phone_when_authencation = false
                signup1Activity.supportFragmentManager.popBackStack()
                return@setOnClickListener
            }

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
        shareViewModel.set_user_info_phone(phone)
        if (country_code != "") {
            shareViewModel.user_info.country_code = country_code
        }
    }

    fun validd_phone_number_and_country_code(phone: String, country_code: String): Boolean {
        if (phone.length < 6) {
            set_error_edittext("Please enter valid phone number , like VietNamese phone number or you can use email address.")
            return false
        }
        if (!check_country_code(country_code)) {
            set_error_edittext("Please just enter valid country code\\n Please enter number in country code and don't enter '+' or charaters.")
            return false
        }
        val list_phone_number = shareViewModel.list_phone_number
        if (list_phone_number.contains(phone)) {
            set_error_edittext("Phone number already use by another user")
            return false
        }
        return true
    }

    fun go_to_frag_auth_phone_number_account(
        phone_number: String,
        country_code: String
    ) {
        val old_phone_number = shareViewModel.user_info.phone
        shareViewModel.set_user_info_phone(phone_number)
        shareViewModel.set_user_info_country_code(country_code)
        shareViewModel.is_user_change_phone_when_authencation = false

        //Add new phone number to list and delete the old phone number
        //And set new phone number in user info
        shareViewModel.list_phone_number.add(phone_number)
        shareViewModel.list_phone_number.remove(old_phone_number)
        shareViewModel.set_user_info_phone(phone_number)

        val account_ref = shareViewModel.account_user

        val ref_user_info_phone_number = database
            .child("User")
            .child(account_ref)
            .child("phone")

        val ref_user_info_country_code = database
            .child("User")
            .child(account_ref)
            .child("country_code")

        val ref_phone_and_email_and_account = database
            .child("phone and email and account")
            .child(shareViewModel.index_of_last_ele_phone_email_account.toString())
            .child("phone")

        var done_phone = false
        var done_country_code = false
        var done_phone_email_account = false

        ref_phone_and_email_and_account.setValue(phone_number)
            .addOnCompleteListener(signup1Activity) { task ->
                if (task.isSuccessful) {
                    done_phone_email_account = true
                    if (done_phone && done_country_code && done_phone_email_account) {
                        //We must make sure that our database is updated and then popBackStack
                        signup1Activity.supportFragmentManager.popBackStack()
                    }
                } else {
                    error_networking_and_move_frag()
                }
            }

        ref_user_info_phone_number.setValue(phone_number)
            .addOnCompleteListener(signup1Activity) { task ->
                if (task.isSuccessful) {
                    done_phone = true
                    if (done_phone && done_country_code && done_phone_email_account) {
                        //We must make sure that our database is updated and then popBackStack
                        signup1Activity.supportFragmentManager.popBackStack()
                    }
                } else {
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
                        if (done_phone && done_country_code && done_phone_email_account) {
                            //We must make sure that our database is updated and then popBackStack
                            signup1Activity.supportFragmentManager.popBackStack()
                        }
                    } else {
                        error_networking_and_move_frag()
                    }
                }
        } else {
            done_country_code = true
            if (done_phone && done_country_code && done_phone_email_account) {
                signup1Activity.supportFragmentManager.popBackStack()
            }
        }
    }

    private fun error_networking_and_move_frag() {
        if (!MyMethod.isWifi(signup1Activity)) {
            val s = "Please connect wifi to continue"
            s.showToastLong(signup1Activity)
        } else {
            val s = "One thing went wrong , but don't worry just continue"
            s.showToastLong(signup1Activity)
            signup1Activity.supportFragmentManager.popBackStack()
        }
    }
}