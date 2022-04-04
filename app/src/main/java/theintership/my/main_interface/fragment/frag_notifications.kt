package theintership.my.main_interface.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import theintership.my.Main_Interface_Activity
import theintership.my.R
import theintership.my.databinding.FragNotificationsBinding


class frag_notifications : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_notifications , container ,false)
        val layout = view.findViewById<SwipeRefreshLayout>(R.id.frag_notificaions_swipelayout)
        val new = view.findViewById<TextView>(R.id.frag_notifications_tv_new)

        new.setOnClickListener {
            layout.isRefreshing = false
        }

        layout.setOnRefreshListener {
            println("debug refresh ne")
        }


        return view
    }

}

