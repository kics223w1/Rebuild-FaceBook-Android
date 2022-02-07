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
import theintership.my.databinding.FragSignup31Binding
import kotlin.math.sign

class frag_signup3_1 : Fragment(R.layout.frag_signup3_1), IReplaceFrag , IToast {

    private var _binding: FragSignup31Binding? = null
    private val binding get() = _binding!!
    private lateinit var signup1Activity: Signup1Activity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignup31Binding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity
        frag_signup2().check_frag_signup3_1 = false

        show("${frag_signup2().check_frag_signup3_1} trong frag_signup3_1" , signup1Activity)

        binding.btnSignup31Go.setOnClickListener {
            replacefrag("frag_signup4", frag_signup4(), signup1Activity.supportFragmentManager)
        }

        binding.btnSignup31Popback.setOnClickListener {
            frag_signup3().check = true
            frag_signup2().check_frag_signup3_1 = true
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