package theintership.my.main_interface.notifications.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import theintership.my.Main_Interface_Activity
import theintership.my.R
import theintership.my.databinding.FragRepliesBinding


class frag_replies : Fragment(R.layout.frag_replies) {

    private var _binding: FragRepliesBinding? = null
    private val binding get() = _binding!!
    private lateinit var Main_Interface_Activity: Main_Interface_Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragRepliesBinding.inflate(inflater, container, false)
        Main_Interface_Activity = activity as Main_Interface_Activity
        binding.btnBack.setOnClickListener {
            Main_Interface_Activity.supportFragmentManager.popBackStack()
        }




        return binding.root
    }

}