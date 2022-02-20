package theintership.my.signin_signup.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import org.w3c.dom.Text
import theintership.my.R
import java.time.Year
import java.util.*

class dialog_signup_age(context : Context , Age : Int) : Dialog(context) {
    init {
        setCancelable(true)
    }
    private val mAge = Age
    lateinit var btn_go : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_signup_age)

        val btn_cancel = findViewById<TextView>(R.id.btn_dialog_signup_age_cancel)
        btn_go = findViewById(R.id.btn_dialog_signup_age_go)
        val tv_info = findViewById<TextView>(R.id.tv_dialog_signup_age_info)

        val today = Calendar.getInstance()
        val day = today.get(Calendar.DAY_OF_MONTH).toString()
        val month = today.get(Calendar.MONTH).toString()
        val year = (today.get(Calendar.YEAR) - mAge).toString()

        tv_info.text = "Bạn đang thiết lập sinh nhật của mình là $day tháng $month, $year"


        btn_cancel.setOnClickListener {
            dismiss()
        }

    }

}