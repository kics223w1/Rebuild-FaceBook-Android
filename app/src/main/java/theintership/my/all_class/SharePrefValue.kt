package theintership.my.all_class

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.res.TypedArrayUtils.getString
import theintership.my.R

class SharePrefValue(context : Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences(
        "the.intership.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE
    )

    fun get_account_ref() : String{
        return sharedPref.getString("account ref" , "").toString()
    }

    fun get_link_avatar() : String{
        return sharedPref.getString("link avatar" , "").toString()
    }

    fun get_user_name() : String{
        return sharedPref.getString("user name" , "").toString()
    }

}