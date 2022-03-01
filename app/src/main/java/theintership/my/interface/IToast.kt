package theintership.my.`interface`

import android.content.Context
import android.widget.Toast
import theintership.my.MainActivity

interface IToast {
    fun show(s : String  , context : Context){
        Toast.makeText(context , s , Toast.LENGTH_SHORT).show()
    }
    fun showLong(s : String , context : Context){
        Toast.makeText(context , s , Toast.LENGTH_LONG).show()
    }
}