package theintership.my.signin_signup.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import theintership.my.R

class dialog_stop_signup(context: Context) : Dialog(context) {
    init {
        setCancelable(true)
    }

    lateinit var btn_cancel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_stop_signup)

        val btn_go = findViewById<TextView>(R.id.btn_dialog_stop_signup_go)
        btn_cancel = findViewById(R.id.btn_dialog_stop_signup_cancel)

        btn_go.setOnClickListener {
            dismiss()
        }

    }

}