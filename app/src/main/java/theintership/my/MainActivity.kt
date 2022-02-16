package theintership.my

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import theintership.my.signin_signup.Signup1Activity


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btn_create_account = findViewById<TextView>(R.id.btn_signin_createAccout)
        val edt_signin_account = findViewById<EditText>(R.id.edt_signin_account)
        val edt_signin_password = findViewById<EditText>(R.id.edt_signin_password)
        val icon_password_nosee = findViewById<ImageView>(R.id.password_nosee)
        val icon_password_see = findViewById<ImageView>(R.id.password_see)
        val btn_signin = findViewById<TextView>(R.id.btn_signin_go)


        edt_signin_password.doAfterTextChanged {
            val password = edt_signin_password.text
            if (!password.isEmpty()) {
                icon_password_see.visibility = View.VISIBLE
            } else {
                icon_password_see.visibility = View.INVISIBLE
                icon_password_nosee.visibility = View.INVISIBLE
            }
        }



        icon_password_see.setOnClickListener {
            if (icon_password_nosee.visibility == View.VISIBLE) {
                icon_password_nosee.visibility = View.INVISIBLE
                edt_signin_password.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                edt_signin_password.setSelection(edt_signin_password.length())
            } else {
                icon_password_nosee.visibility = View.VISIBLE
                edt_signin_password.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                edt_signin_password.setSelection(edt_signin_password.length())
            }
        }

        btn_create_account.setOnClickListener {
            startActivity(Intent(this, Signup1Activity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }

    fun showSoftKeyboard(view: View) {
        val inputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }

}