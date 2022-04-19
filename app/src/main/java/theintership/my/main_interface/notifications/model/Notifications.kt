package theintership.my.main_interface.notifications.model

import android.graphics.Bitmap
import android.text.SpannableString
import androidx.room.PrimaryKey
import com.google.firebase.database.snapshot.BooleanNode
import theintership.my.R
import theintership.my.all_class.MyMethod.Companion.blackout_char
import theintership.my.all_class.MyMethod.Companion.get_AM_or_PM
import theintership.my.all_class.MyMethod.Companion.get_day_of_week
import theintership.my.all_class.MyMethod.Companion.get_hour
import theintership.my.all_class.MyMethod.Companion.get_minutes


//List kind of notifcations:
//React post ( like , love , haha , sad , love love)
//Reply comment
//Post in group
//Request friend

data class Notifications(
    var to_person: String?,
    var day_and_time: String?,
    var day_create: String?,
    var from_person: String?,
    var content: String?,
    var kind_of_noti: String?,
    var link_post: String?,
    var id_comment: Int?,
    var group: String?,
    var icon: String?,
    var link_avatar_person: String?,
    var is_readed: Boolean
) {
    fun black_out_content(): SpannableString {
        val list = mutableListOf<String>()
        list.add(this.from_person.toString())
        if (group != "") {
            list.add(this.group.toString())
        }
        val span = blackout_char(this.content.toString(), list)
        return span
    }


    fun set_day_and_time() {
        var day = get_day_of_week()
        day = day.substring(0, 3)
        val hour = get_hour()
        val minutes = get_minutes()
        val am_or_pm = get_AM_or_PM()
        var ans = ""
        ans += day
        ans += " at "
        ans += hour
        ans += ":"
        ans += minutes
        ans += " "
        ans += am_or_pm
        this.day_and_time = ans
    }


}


