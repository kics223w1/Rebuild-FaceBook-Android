package theintership.my.`interface`

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import theintership.my.R

interface IReplaceFrag {
    fun replacefrag(tag : String, frag : Fragment, fm : FragmentManager){
        fm.beginTransaction().addToBackStack(tag).replace(R.id.layout_mainactivity , frag).commit()
    }
}