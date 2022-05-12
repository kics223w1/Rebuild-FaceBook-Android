package theintership.my.main_interface.notifications.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import theintership.my.activity.Main_Interface_Activity
import theintership.my.R
import theintership.my.databinding.FragPostBinding


class frag_post : Fragment(R.layout.frag_post) {

    private var _binding: FragPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var Main_Interface_Activity: Main_Interface_Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragPostBinding.inflate(inflater, container, false)
        Main_Interface_Activity = activity as Main_Interface_Activity
        binding.btnBack.setOnClickListener {
            Main_Interface_Activity.supportFragmentManager.popBackStack()
        }




        return binding.root
    }

}