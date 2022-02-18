package theintership.my.signin_signup
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast


class frag_signup_age : Fragment(R.layout.frag_signup_age), IReplaceFrag , IToast {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.frag_signup_age , container , false)
        val signup1Activity = activity as Signup1Activity
        val btnSignupAgeGo = view.findViewById<TextView>(R.id.btn_signup_age_go)
        val btnSignupAgePopback = view.findViewById<TextView>(R.id.btn_signup_age_popback)
        val btnSignupAgeBack = view.findViewById<TextView>(R.id.btn_signup_age_back)

        signup1Activity.go_to_frag_signup3_1 = true

        btnSignupAgeGo.setOnClickListener {
            replacefrag("frag_signup4", frag_signup_sex(), signup1Activity.supportFragmentManager)
        }

        btnSignupAgePopback.setOnClickListener {
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

        btnSignupAgeBack.setOnClickListener {
            val dialog = dialog_cancel_signup(signup1Activity)
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

        return view
    }

}