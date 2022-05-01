package theintership.my.main_interface.friends.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import theintership.my.main_interface.friends.model.Friends
import theintership.my.main_interface.notifications.model.Notifications

class ViewModelFragFriends : ViewModel() {



    fun setup_friends(snapshot: DataSnapshot): Friends{
        val it = snapshot
        val name = it.child("name").getValue()?.toString()
        val account_ref = it.child("account_ref").getValue()?.toString()
        val link_avatar = it.child("link_avatar").getValue()?.toString()
        val day = it.child("day").getValue()?.toString()
        val hour = it.child("hour").getValue()?.toString()
        val fr = Friends(name , account_ref , link_avatar , day , hour)
        return fr
    }

}