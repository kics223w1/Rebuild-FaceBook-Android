package theintership.my.signin_signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.databinding.FragSignup1Binding

class frag_signup1 : Fragment(R.layout.frag_signup1), IReplaceFrag {

    private var _binding: FragSignup1Binding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignup1Binding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity

        binding.btnSignup1Go.setOnClickListener {
            replacefrag(
                tag = "frag_signup2",
                frag = frag_signup2(),
                fm = mainActivity.supportFragmentManager
            )
        }

        println("Huyhuy")

        binding.btnSignup1Back.setOnClickListener {
            mainActivity.supportFragmentManager.popBackStack()
        }


        return binding.root
    }

}