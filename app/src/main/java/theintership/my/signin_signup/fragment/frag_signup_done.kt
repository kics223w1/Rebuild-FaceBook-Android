package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSignupDoneBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.viewModel_Signin_Signup


class frag_signup_done : Fragment(R.layout.frag_signup_done), IReplaceFrag, IToast {

    private var _binding: FragSignupDoneBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private var database: DatabaseReference = Firebase.database.reference
    private val viewmodelSigninSignup : viewModel_Signin_Signup by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupDoneBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity


        binding.btnSignupDoneGo.setOnClickListener {
            val phone = viewmodelSigninSignup.User.phone.toString()
            val email = viewmodelSigninSignup.User.email.toString()



            check_phone_and_email(phone , email)
        }

        binding.btnSignupDoneGo2.setOnClickListener {
            val phone = viewmodelSigninSignup.User.phone.toString()
            val email = viewmodelSigninSignup.User.email.toString()
            check_phone_and_email(phone , email)
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

    private fun move_to_frag_create_account(){
        replacefrag(
            "frag_signup_creating_account",
            frag_signup_creating_account(),
            signup1activity.supportFragmentManager
        )
    }

    private fun check_phone_and_email(phone : String , email : String){
        val myref = database.child("phone and email")
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children
                list.forEach {
                    val mphone = it.child("phone").getValue().toString()
                    val memail = it.child("email").getValue().toString()
                    if (mphone == phone){
                        viewmodelSigninSignup.same_phone = true
                    }
                    if (memail == email){
                        viewmodelSigninSignup.same_email = true
                    }
                }
                //Why this function must be here
                //Because firebase function always the last thing program does
                //So i must do what i want to do in firebase function
                move_to_frag_create_account()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        myref.addValueEventListener(postListener)
    }

}