package theintership.my.signin_signup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.R
import theintership.my.`interface`.ICheckWifi
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSigningAccountBinding
import theintership.my.model.Phone_and_Email
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.viewModel_Signin_Signup


class frag_signing_account : Fragment(R.layout.frag_signing_account), IReplaceFrag , IToast , ICheckWifi{

    private var _binding: FragSigningAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private lateinit var database: DatabaseReference
    private val viewmodelSigninSignup: viewModel_Signin_Signup by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSigningAccountBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity
        database = Firebase.database.reference
        val ref_user = database.child("User")
        val ref_phone_and_email = database.child("phone and email")
        val user = viewmodelSigninSignup.User
        val phoneAndEmail = Phone_and_Email(user.email.toString(), user.phone.toString())
        val email_ref = get_email_ref(user.email.toString())

        ref_phone_and_email.child(email_ref).setValue(phoneAndEmail)
            .addOnCompleteListener(signup1activity) { task ->
                if (task.isSuccessful) {
                    ref_user.child(email_ref).setValue(user)
                        .addOnCompleteListener(signup1activity) { task2 ->
                            if (task2.isSuccessful) {
                                move_to_frag_authencation_account()
                            }else{
                                error_network()
                            }
                        }
                }else{
                    error_network()
                }
            }


        return binding.root
    }

    private fun get_email_ref(email: String): String {
        var ans = ""
        for (i in 0 until email.length) {
            if (email[i] == '@') {
                break
            }
            ans += email[i]
        }
        return ans
    }


    private fun move_to_frag_authencation_account() {
        replacefrag(
            "frag_authencation_account",
            frag_authencation_account(),
            signup1activity.supportFragmentManager
        )
    }

    private fun error_network(){
        if (isWifi(signup1activity)){
            showLong("Connect wifi is disrupted , pls connect wifi and sign up again" , signup1activity)
        }else{
            showLong("Some thing with our sever went wrong . Sorry for the error . Pls sign up again" , signup1activity)
        }
    }


}