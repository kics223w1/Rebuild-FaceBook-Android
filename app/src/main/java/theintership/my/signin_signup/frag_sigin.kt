package theintership.my.signin_signup

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.databinding.FragSigninBinding

class frag_sigin : Fragment(R.layout.frag_signin), IReplaceFrag {

    private var _binding: FragSigninBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSigninBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity

        binding.btnCreateAccoutSignin.setOnClickListener {
            replacefrag(
                tag = "frag_signup1",
                frag = frag_signup1(),
                fm = mainActivity.supportFragmentManager
            )
        }

        return binding.root
    }


    fun showSoftKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }


}