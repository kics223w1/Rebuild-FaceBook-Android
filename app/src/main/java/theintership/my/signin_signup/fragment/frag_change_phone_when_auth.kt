package theintership.my.signin_signup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import theintership.my.MyMethod
import theintership.my.MyMethod.Companion.hide_soft_key_board
import theintership.my.MyMethod.Companion.showToastLong
import theintership.my.R
import theintership.my.databinding.FragChangeEmailWhenAuthBinding
import theintership.my.databinding.FragChangePhoneWhenAuthBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.shareViewModel

class frag_change_phone_when_auth : Fragment(R.layout.frag_change_phone_when_auth) {

    private var _binding: FragChangePhoneWhenAuthBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private var database: DatabaseReference = Firebase.database.reference
    private val shareViewModel: shareViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragChangePhoneWhenAuthBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity

        binding.btnChangePhoneWhenAuthBack.setOnClickListener {
            hide_soft_key_board(signup1activity , binding.btnChangePhoneWhenAuthBack)
            signup1activity.supportFragmentManager.popBackStack()
        }

        binding.btnChangePhoneWhenAuthChange.setOnClickListener {
            val phone_number = binding.edtChangePhoneWhenAuth.text.toString()
            val country_code = binding.edtChangePhoneWhenAuthCountryCode.text.toString()
            hide_soft_key_board(signup1activity , binding.btnChangePhoneWhenAuthChange)

            if (validd_phone_number_and_country_code(phone = phone_number , country_code = country_code))
                go_to_frag_auth_phone_number_account(phone_number, country_code)

        }

        binding.edtChangePhoneWhenAuthCountryCode.setOnEditorActionListener{
            textView , i , keyEvent ->
            val phone_number = binding.edtChangePhoneWhenAuth.text.toString()
            val country_code = binding.edtChangePhoneWhenAuthCountryCode.text.toString()
            hide_soft_key_board(signup1activity , binding.btnChangePhoneWhenAuthChange)
            if (validd_phone_number_and_country_code(phone = phone_number , country_code = country_code)) {
                go_to_frag_auth_phone_number_account(phone_number, country_code)
                true
            }else{
                false
            }
        }

        return binding.root
    }


    private fun validd_phone_number_and_country_code(phone: String, country_code: String): Boolean {
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
        move_error_edittext()
        return true
    }

    private fun go_to_frag_auth_phone_number_account(
        phone_number: String,
        country_code: String
    ) {
        val old_phone_number = shareViewModel.user_info.phone
        shareViewModel.set_user_info_phone(phone_number)
        shareViewModel.set_user_info_country_code(country_code)

        //Add new phone number to list and delete the old phone number
        //And set new phone number in user info
        shareViewModel.list_phone_number.add(phone_number)
        shareViewModel.list_phone_number.remove(old_phone_number)
        shareViewModel.set_user_info_phone(phone_number)

        set_loading_process()

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

        CoroutineScope(Dispatchers.IO).launch {
            ref_phone_and_email_and_account.setValue(phone_number)
                .addOnCompleteListener(signup1activity) { task ->
                    if (task.isSuccessful) {
                        done_phone_email_account = true
                        if (done_phone && done_country_code && done_phone_email_account) {
                            //We must make sure that our database is updated and then popBackStack
                            remove_loading_process()
                            signup1activity.supportFragmentManager.popBackStack()
                        }
                    } else {
                        error_networking_and_move_frag()
                    }
                }
        }

        CoroutineScope(Dispatchers.IO).launch {
            ref_user_info_phone_number.setValue(phone_number)
                .addOnCompleteListener(signup1activity) { task ->
                    if (task.isSuccessful) {
                        done_phone = true
                        if (done_phone && done_country_code && done_phone_email_account) {
                            //We must make sure that our database is updated and then popBackStack
                            remove_loading_process()
                            signup1activity.supportFragmentManager.popBackStack()
                        }
                    } else {
                        error_networking_and_move_frag()
                    }
                }
        }


        if (country_code != "") {
            //Defaut country code is 84 so if user don't enter country code ,
            //so we leave it is 84
            CoroutineScope(Dispatchers.IO).launch {
                ref_user_info_country_code.setValue(country_code)
                    .addOnCompleteListener(signup1activity) { task ->
                        if (task.isSuccessful) {
                            done_country_code = true
                            if (done_phone && done_country_code && done_phone_email_account) {
                                //We must make sure that our database is updated and then popBackStack
                                remove_loading_process()
                                signup1activity.supportFragmentManager.popBackStack()
                            }
                        } else {
                            error_networking_and_move_frag()
                        }
                    }
            }
        } else {
            done_country_code = true
            if (done_phone && done_country_code && done_phone_email_account) {
                remove_loading_process()
                signup1activity.supportFragmentManager.popBackStack()
            }
        }
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

    private fun set_error_edittext(str: String) {
        binding.tvChangePhoneWhenAuthInfo.text = str
        binding.tvChangePhoneWhenAuthInfo.setTextColor(resources.getColor(R.color.error, null))
    }

    private fun move_error_edittext() {
        binding.tvChangePhoneWhenAuthInfo.text =
            "Please enter your phone number. You can always make this private later"
        binding.tvChangePhoneWhenAuthInfo.setTextColor(resources.getColor(R.color.light_grey, null))
    }

    private fun set_loading_process(){
        binding.progressBarChangePhoneWhenAuth.visibility = View.VISIBLE
        binding.progressBarChangePhoneWhenAuth.visibility = View.INVISIBLE
    }

    private fun remove_loading_process(){
        binding.progressBarChangePhoneWhenAuth.visibility = View.VISIBLE
        binding.progressBarChangePhoneWhenAuth.visibility = View.INVISIBLE
    }

    private fun error_networking_and_move_frag() {
        if (!MyMethod.isWifi(signup1activity)) {
            val s = "Please connect wifi to continue"
            s.showToastLong(signup1activity)
        } else {
            val s = "One thing went wrong , but don't worry just continue"
            s.showToastLong(signup1activity)
            signup1activity.supportFragmentManager.popBackStack()
        }
    }
}