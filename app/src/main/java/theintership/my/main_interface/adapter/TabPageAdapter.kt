package theintership.my.main_interface.adapter

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import theintership.my.R
import theintership.my.main_interface.fragment.*

class TabPageAdapter(activity: FragmentActivity, private val tab_count: Int) :
    FragmentStateAdapter(activity) {
    private val context = activity
    override fun getItemCount(): Int = tab_count

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> frag_home()
            1->frag_group()
            2->frag_video()
            3->frag_heart()
            4->frag_notifications()
            5->frag_setting()
            else -> frag_home()
        }
    }

    fun set_custom_icon_notificaion() : View {
        val v = LayoutInflater.from(context).inflate(R.layout.custom_icon_notification , null)
        return v
    }
}