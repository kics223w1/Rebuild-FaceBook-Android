package theintership.my.main_interface.profile_owner.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import theintership.my.main_interface.profile_owner.model.friend_in_profile
import kotlin.math.min

class ViewModelFragProfileOwner : ViewModel() {

    fun setup_list_friend_in_profile(snapshot: DataSnapshot): MutableList<friend_in_profile> {
        val list = snapshot.children.toMutableList()
        val ans = mutableListOf<friend_in_profile>()
        for (i in 0 until min(list.size , 6)  step 3) {
            val name1 = list[i].child("name").getValue().toString()
            val link_avatar1 = list[i].child("link_avatar").getValue().toString()
            var name2 = ""
            var link_avatar2 = ""
            var name3 = ""
            var link_avatar3 = ""

            if (i + 1 < list.size) {
                name2 = list[i + 1].child("name").getValue().toString()
                link_avatar2 = list[i + 1].child("link_avatar").getValue().toString()
            }
            if (i + 2 < list.size) {
                name3 = list[i + 2].child("name").getValue().toString()
                link_avatar3 = list[i + 2].child("link_avatar").getValue().toString()
            }
            val fr =
                friend_in_profile(name1, link_avatar1, name2, link_avatar2, name3, link_avatar3)
            ans.add(fr)
        }
        return ans
    }


}