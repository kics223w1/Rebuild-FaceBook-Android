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
import theintership.my.databinding.FragSignupCreateAccountBinding
import theintership.my.model.User
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_cannot_create_account
import theintership.my.signin_signup.viewModel_Signin_Signup


class frag_signup_create_account : Fragment(R.layout.frag_signup_create_account) {

    private var _binding: FragSignupCreateAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity
    private val viewmodelSigninSignup : viewModel_Signin_Signup by activityViewModels()
    private var database: DatabaseReference = Firebase.database.reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupCreateAccountBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity
        var create_success = false
        val user = viewmodelSigninSignup.User
        val phone_user = user.phone
        val email_user = user.email

        if (phone_user == "" && email_user == ""){
            val dialog = dialog_cannot_create_account(signup1activity)
            dialog.show()
            dialog.btn_stop.setOnClickListener {
                startActivity(Intent(signup1activity, MainActivity::class.java))
                signup1activity.overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                dialog.dismiss()
            }
        }


        if (create_success){

            show_icon_success()
        }



        return binding.root
    }

    private fun show_icon_success(){
        binding.progressCreateAccount.visibility = View.GONE
        binding.tvFragSignupCreateAccountInfo.visibility = View.GONE
        binding.iconCreateAccountSuccess.visibility = View.VISIBLE
        binding.iconCreateAccountSuccess.animate().apply {
            duration = 1500
            scaleX(2.5F)
            scaleY(2.5F)
        }.withEndAction {
            binding.iconCreateAccountSuccess.animate().apply {
                duration = 1000
                scaleX(1.5F)
                scaleY(1.5F)
            }.withEndAction {
                binding.iconCreateAccountSuccess.animate().apply {
                    duration = 1500
                    scaleX(2.5F)
                    scaleY(2.5F)
                }.withEndAction{
                    binding.iconCreateAccountSuccess.animate().apply {
                        duration = 1000
                        scaleX(1.5F)
                        scaleY(1.5F)
                    }.start()
                }
            }
        }
    }

    private fun add_user_and_phone_email(user : User){
        val ref_user = database.child("User")
        val ref_phone_and_email =database.child("phone and email")
        ref_phone_and_email.orderByKey().limitToLast(1).addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val list = snapshot.children
                println("debug $list")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val id = user.id.toString()
        ref_user.child(id).setValue(user)
    }

}