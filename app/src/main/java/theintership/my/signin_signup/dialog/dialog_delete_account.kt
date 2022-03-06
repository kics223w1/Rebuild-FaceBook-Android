package theintership.my.signin_signup.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import theintership.my.R

class dialog_delete_account(context: Context) : Dialog(context) {

    lateinit var btn_delete: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_delete_account)

        val btn_keep = findViewById<TextView>(R.id.btn_dialog_delete_account_keep)
        btn_delete = findViewById(R.id.btn_dialog_delete_account_delete)

        btn_keep.setOnClickListener {
            dismiss()
        }
    }
}