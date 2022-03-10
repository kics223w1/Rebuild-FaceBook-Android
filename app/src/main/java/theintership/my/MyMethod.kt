package theintership.my

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

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


        fun replacefrag(tag: String, frag: Fragment, fm: FragmentManager) {
            fm.beginTransaction().addToBackStack(tag).replace(R.id.layout_Signup1Activity, frag)
                .commit()
        }
    }
}