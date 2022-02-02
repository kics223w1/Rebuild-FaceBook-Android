package theintership.my.signin_signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.databinding.FragSignup2Binding

class frag_signup2 : Fragment(R.layout.frag_signup2) , IReplaceFrag {

    private var _binding : FragSignup2Binding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignup2Binding.inflate(inflater , container , false)
        mainActivity = activity as MainActivity

        binding.btnSignup2Go.setOnClickListener {
            replacefrag(
                tag = "frag_signup3",
                frag = frag_signup3(),
                fm = mainActivity.supportFragmentManager
            )
        }

        binding.btnSignup2Back.setOnClickListener {
            mainActivity.supportFragmentManager.popBackStack()
        }

        return binding.root
    }

}