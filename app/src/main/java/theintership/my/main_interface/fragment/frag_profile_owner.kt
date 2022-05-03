package theintership.my.main_interface.fragment

import android.graphics.Outline
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.R
import theintership.my.databinding.FragProfileOwnerBinding


class frag_profile_owner : Fragment() {

    private var _binding : FragProfileOwnerBinding? =null
    private val binding get() = _binding!!
    private val database: DatabaseReference = Firebase.database.reference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragProfileOwnerBinding.inflate(inflater , container , false)
        set_boder_cover_image()


        return binding.root
    }

    private fun set_boder_cover_image(){
        val curveRadius = 20F
        binding.coverImage.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                outline?.setRoundRect(0, 0, view!!.width, (view.height+curveRadius).toInt(), curveRadius)
            }
        }
        binding.coverImage.clipToOutline = true
    }

}
