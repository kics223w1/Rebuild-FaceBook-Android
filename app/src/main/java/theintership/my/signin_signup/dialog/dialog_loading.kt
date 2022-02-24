package theintership.my.signin_signup.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import theintership.my.R

class dialog_loading(context : Context) : Dialog(context) {

   init {
       setCancelable(false)
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_loading)

    }
}