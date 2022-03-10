package theintership.my.signin_signup.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import theintership.my.R

class dialog_show_account_and_password(context : Context) : Dialog(context) {

    lateinit var btn_go : TextView
    lateinit var tv_account : TextView
    lateinit var tv_password : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_show_account_and_password)

        btn_go = findViewById(R.id.btn_dialog_show_account_and_password_go)
        tv_account = findViewById(R.id.tv_dialog_show_account_and_password_tvaccount)
        tv_password = findViewById(R.id.tv_dialog_show_account_and_password_tvpassword)

    }

}