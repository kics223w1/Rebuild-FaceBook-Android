package theintership.my.all_class

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import theintership.my.R
import java.util.*

class MyMethod {
    companion object {
        fun String.showToastShort(context: Context) {
            Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
        }

        fun String.showToastLong(context: Context) {
            Toast.makeText(context, this, Toast.LENGTH_LONG).show()
        }

        fun isWifi(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {

                    return true
                }
            }
            return false
        }

        fun check_wifi(context: Context) : Boolean{
            if (!isWifi(context)) {
                val s = "Please connect wifi to continue"
                s.showToastLong(context)
                return false
            }
            return true
        }

        fun replacefrag(tag: String, frag: Fragment, fm: FragmentManager) {
            fm.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in , android.R.anim.fade_out)
                .addToBackStack(tag)
                .replace(R.id.layout_Signup1Activity, frag)
                .commit()
        }

        fun addfrag(tag: String, frag: Fragment, fm: FragmentManager) {
            fm.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in , android.R.anim.fade_out)
                .addToBackStack(tag)
                .add(R.id.layout_Signup1Activity, frag , tag)
                .commit()
        }

        fun replacefrag_by_silde_in_left(tag: String, frag: Fragment, fm: FragmentManager) {
            fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .addToBackStack(tag)
                .replace(R.id.layout_Signup1Activity, frag)
                .commit()
        }

        fun hide_soft_key_board(context: Context, view: View) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }

        fun set_today(): String {
            var mtoday = Calendar.getInstance()
            var today = ""
            today += (mtoday.get(Calendar.MONTH) + 1).toString()
            today += "/"
            today += mtoday.get(Calendar.DAY_OF_MONTH).toString()
            today += "/"
            today += mtoday.get(Calendar.YEAR).toString()

            return today
        }


    }
}