package theintership.my.signin_signup.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import org.w3c.dom.Text
import theintership.my.R

class dialog_cannot_create_account(context : Context , phone_or_email : String) : Dialog(context) {

    init {
        setCancelable(false)
    }

    val mphone_or_email = phone_or_email
    lateinit var btn_stop : TextView
    lateinit var btn_find : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_cannot_create_account)

        val tv_info = findViewById<TextView>(R.id.tv_dialog_cannot_create_account_info)
        btn_stop = findViewById(R.id.btn_dialog_cannot_createAccount_stop)
        btn_find = findViewById(R.id.btn_dialog_cannot_createAccount_find)

        tv_info.text = "$mphone_or_email đã được sử dụng. Có thể đó là tài khoản cũ của bạn."

    }

}