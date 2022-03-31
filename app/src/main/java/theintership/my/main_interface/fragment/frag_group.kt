package theintership.my.main_interface.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import theintership.my.Main_Interface_Activity
import theintership.my.R
import theintership.my.databinding.FragGroupBinding


class frag_group : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.frag_group , container ,false)
    }

}