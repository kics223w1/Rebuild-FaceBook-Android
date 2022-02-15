package theintership.my.signin_signup
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSignupAgeBinding


class frag_signup_age : Fragment(R.layout.frag_signup_age), IReplaceFrag , IToast {

    private var _binding: FragSignupAgeBinding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupAgeBinding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity
        signup1Activity.go_to_frag_signup3_1 = true


        binding.btnSignup31Go.setOnClickListener {
            replacefrag("frag_signup4", frag_signup_sex(), signup1Activity.supportFragmentManager)
        }

        binding.btnSignup31Popback.setOnClickListener {
            val size = signup1Activity.supportFragmentManager.backStackEntryCount
            val frag = signup1Activity.supportFragmentManager.getBackStackEntryAt(size - 2)
            //size always >= 2
            //check when user click backpress before and program will pop 2 frag
            //so frag_signup3 is not in backStack
            if (frag.name == "frag_signup2"){
                signup1Activity.supportFragmentManager.popBackStack()
                replacefrag("frag_signup3" ,frag_signup_birthday() , signup1Activity.supportFragmentManager)
                return@setOnClickListener
            }
            signup1Activity.go_to_frag_signup3_1 = false
            signup1Activity.supportFragmentManager.popBackStack()
        }

        binding.btnSignup31Back.setOnClickListener {
            val dialog = dialog(signup1Activity)
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

}