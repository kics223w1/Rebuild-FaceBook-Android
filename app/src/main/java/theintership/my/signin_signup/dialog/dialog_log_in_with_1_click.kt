package theintership.my.signin_signup.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import theintership.my.R

class dialog_log_in_with_1_click(context : Context) : Dialog(context) {

    lateinit var btn_save : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_log_in_with_1_click)

        val btn_cancel = findViewById<TextView>(R.id.btn_dialog_log_in_with_1_click_cancel)
        btn_save = findViewById(R.id.btn_dialog_log_in_with_1_click_save)

        btn_cancel.setOnClickListener {
            dismiss()
        }


    }

}