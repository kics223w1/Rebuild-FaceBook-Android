package theintership.my.signin_signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.databinding.FragSignup3Binding

class frag_signup3 : Fragment(R.layout.frag_signup3) , IReplaceFrag {

    private var _binding : FragSignup3Binding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignup3Binding.inflate(inflater , container , false)
        mainActivity = activity as MainActivity

        binding.btnSignup3Go.setOnClickListener {
            replacefrag(
                tag = "frag_signup4",
                frag = frag_signup4(),
                fm = mainActivity.supportFragmentManager
            )
        }

        binding.btnSignup3Back.setOnClickListener {
            mainActivity.supportFragmentManager.popBackStack()
        }

        return binding.root
    }

}