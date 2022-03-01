package theintership.my.signin_signup.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import org.w3c.dom.Text
import theintership.my.R

class dialog_cannot_create_account(context : Context) : Dialog(context) {

    init {
        setCancelable(false)
    }

    lateinit var btn_stop : TextView
    lateinit var btn_find : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_cannot_create_account)

        btn_stop = findViewById(R.id.btn_dialog_cannot_createAccount_stop)
        btn_find = findViewById(R.id.btn_dialog_cannot_createAccount_find)

    }

}