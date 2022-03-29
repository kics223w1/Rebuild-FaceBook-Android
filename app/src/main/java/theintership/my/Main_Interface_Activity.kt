package theintership.my

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import theintership.my.all_class.MyMethod.Companion.showToastShort

class Main_Interface_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_interface)

        val welcom = "Welcom to the intership."
        welcom.showToastShort(this)
    }

}