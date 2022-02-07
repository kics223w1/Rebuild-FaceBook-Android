package theintership.my

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import theintership.my.signin_signup.Signup1Activity

//import theintership.my.signin_signup.frag_signin

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<TextView>(R.id.btn_signin_createAccout)

        btn.setOnClickListener {
            startActivity(Intent(this , Signup1Activity::class.java))
            overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left)
        }



    }

    fun showSoftKeyboard(view: View) {
        val inputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }

}