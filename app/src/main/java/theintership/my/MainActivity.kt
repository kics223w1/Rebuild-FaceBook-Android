package theintership.my

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import theintership.my.all_class.MyMethod.Companion.hide_soft_key_board
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.signin_signup.dialog.dialog_showlanguage

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 100
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_create_account = findViewById<TextView>(R.id.btn_signin_createAccout)
        val edt_signin_account = findViewById<EditText>(R.id.edt_signin_account)
        val edt_signin_password = findViewById<EditText>(R.id.edt_signin_password)
        val icon_password_nosee = findViewById<ImageView>(R.id.password_nosee)
        val icon_password_see = findViewById<ImageView>(R.id.password_see)
        val btn_signin = findViewById<TextView>(R.id.btn_signin_go)
        val btn_showlanguage = findViewById<TextView>(R.id.btn_signin_showlanguage)
        auth = Firebase.auth

        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )


        val check_user_save_password = sharedPref.getBoolean("User save password", false)
        println("debug check user save password : $check_user_save_password")


        btn_showlanguage.setOnClickListener {
            val dialog = dialog_showlanguage(this)
            dialog.show()
        }


        edt_signin_password.setOnEditorActionListener{
            textView , i, Keyevent ->
            hide_soft_key_board(this , btn_signin)
            var account = edt_signin_account.text.toString()
            val password = edt_signin_password.text.toString()
            if (account == "") {
                val s = "Please enter account."
                s.showToastShort(this)
                false
            }else if (password == "") {
                val s = "Please enter password."
                s.showToastShort(this)
                false
            }else{
                account += "@gmail.com"
                signin_user(account = account , password = password)
                true
            }
        }

        btn_signin.setOnClickListener {
            hide_soft_key_board(this , btn_signin)
            var account = edt_signin_account.text.toString()
            val password = edt_signin_password.text.toString()
            if (account == "") {
                val s = "Please enter account."
                s.showToastShort(this)
                return@setOnClickListener
            }
            if (password == "") {
                val s = "Please enter password."
                s.showToastShort(this)
                return@setOnClickListener
            }
            account += "@gmail.com"
            signin_user(account = account , password = password)
        }

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
            this.finish()
        }

    }

    private fun signin_user(account: String, password: String) {
        println("debug vao signin user")
        auth.signInWithEmailAndPassword(account, password)
            .addOnSuccessListener {
                go_to_main_interface()
            }.addOnFailureListener{
                val s = "Password or Account is incorrect."
                s.showToastShort(this)
            }
    }


    private fun go_to_main_interface(){
        startActivity(Intent(this, Main_Interface_Activity::class.java))
        this.overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        this.finish()
    }


    fun showSoftKeyboard(view: View) {
        val inputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }

}