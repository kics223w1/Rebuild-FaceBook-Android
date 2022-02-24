package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.databinding.FragSignupDoneBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_stop_signup
import kotlin.math.sign


class frag_signup_done : Fragment(R.layout.frag_signup_done), IReplaceFrag {

    private var _binding: FragSignupDoneBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1activity: Signup1Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupDoneBinding.inflate(inflater, container, false)
        signup1activity = activity as Signup1Activity

        binding.btnSignupDoneGo.setOnClickListener {
            replacefrag(
                "frag_signup_create_account",
                frag_signup_create_account(),
                signup1activity.supportFragmentManager
            )
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

}