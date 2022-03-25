package theintership.my

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.MyMethod.Companion.showToastLong
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_showlanguage


class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 100

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

        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

       check_permission_read_image()

        val check_user_save_password = sharedPref.getBoolean("User save password" , false)
        println("debug check user save password : $check_user_save_password")


        btn_showlanguage.setOnClickListener {
            val dialog = dialog_showlanguage(this)
            dialog.show()
        }

       btn_signin.setOnClickListener {

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
        }

    }


    private fun check_permission_read_image() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                val s = "Granted"
                s.showToastLong(this)
            }
            else-> {
                val s = "Not granted"
                s.showToastLong(this)
                requestPermissions( arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE) , REQUEST_CODE)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE->{
                if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    println("debug vao request code va granted")
                }else{
                    println("debug vao request code ma khong granted")
                }
                return
            }else->{
                println("debug khac request code")
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun showSoftKeyboard(view: View) {
        val inputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }

}