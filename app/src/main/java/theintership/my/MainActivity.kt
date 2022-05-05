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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import theintership.my.all_class.MyMethod.Companion.get_day_of_week
import theintership.my.all_class.MyMethod.Companion.hide_soft_key_board
import theintership.my.all_class.MyMethod.Companion.set_today
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.signin_signup.dialog.dialog_showlanguage
import theintership.my.signin_signup.model.Email_Account
import theintership.my.signin_signup.model.user_info

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 100
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var progress_signin: ProgressBar
    private lateinit var tv_signin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_create_account = findViewById<TextView>(R.id.btn_signin_createAccout)
        val edt_signin_account = findViewById<EditText>(R.id.edt_signin_account)
        val edt_signin_password = findViewById<EditText>(R.id.edt_signin_password)
        val icon_password_line = findViewById<ImageView>(R.id.password_line)
        val icon_password_eye = findViewById<ImageView>(R.id.password_eye)
        val btn_signin = findViewById<FrameLayout>(R.id.btn_signin_go)
        val btn_forgot_password = findViewById<TextView>(R.id.btn_forgot_password)
        progress_signin = findViewById<ProgressBar>(R.id.progress_signin)
        tv_signin = findViewById<TextView>(R.id.tv_signin)
        auth = Firebase.auth
        database = Firebase.database.reference

        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

        btn_forgot_password.setOnClickListener {
            create_100account()
        }

        val check_user_save_password = sharedPref.getBoolean("User save password", false)


        edt_signin_password.setOnEditorActionListener { textView, i, Keyevent ->
            hide_soft_key_board(this, btn_signin)
            var account = edt_signin_account.text.toString()
            val password = edt_signin_password.text.toString()
            if (account == "") {
                val s = "Please enter account."
                s.showToastShort(this)
                false
            } else if (password == "") {
                val s = "Please enter password."
                s.showToastShort(this)
                false
            } else {
                account += "@gmail.com"
                signin_user(account = account, password = password)
                true
            }
        }

        btn_signin.setOnClickListener {
            hide_soft_key_board(this, btn_signin)
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
            tv_signin.visibility = View.INVISIBLE
            progress_signin.visibility = View.VISIBLE
            signin_user(account = account, password = password)
        }

        edt_signin_password.doAfterTextChanged {
            val password = edt_signin_password.text
            if (!password.isEmpty()) {
                icon_password_line.visibility = View.VISIBLE
                icon_password_eye.visibility = View.VISIBLE
            } else {
                icon_password_line.visibility = View.INVISIBLE
                icon_password_eye.visibility = View.INVISIBLE
            }
        }


        icon_password_eye.setOnClickListener {
            if (icon_password_line.visibility == View.VISIBLE) {
                icon_password_line.visibility = View.GONE
                edt_signin_password.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                edt_signin_password.setSelection(edt_signin_password.length())
            } else {
                icon_password_line.visibility = View.VISIBLE
                edt_signin_password.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                edt_signin_password.setSelection(edt_signin_password.length())
            }
        }

        icon_password_line.setOnClickListener {
            edt_signin_password.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            edt_signin_password.setSelection(edt_signin_password.length())
            icon_password_line.visibility = View.GONE
        }


        btn_create_account.setOnClickListener {
            startActivity(Intent(this, Signup1Activity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            this.finish()
        }

    }

    private fun create_100account() {
        for (i in 4 until 105) {
            val account = "admin" + i
            val email = account + "@gmail.com"
            val password = "huyhuy"
            val ref = database.child("User")
                .child(account)
                .child("user info")
            val ref22 = database.child("User")
                .child(account)
            ref22.child("link avatar")
                .setValue("https://firebasestorage.googleapis.com/v0/b/the-intership.appspot.com/o/avatar_user%2Fadmin1?alt=media&token=e555a00b-63ac-4297-845b-16dcbd371d45")
            ref22.child("link cover image")
                .setValue("https://firebasestorage.googleapis.com/v0/b/the-intership.appspot.com/o/cover_image%2Fadmin1?alt=media&token=5e9d3275-2230-475e-9ce1-f47c6aa9c220")
            val user_info: user_info = user_info(
                email = "admin" + i + "@gmail.com",
                fullname = "Admin Test " + i,
                sex = "Male",
                pronoun = "",
                gender = "",
                age = 21,
                birthday = "18/12/2001",
                create_at = set_today(),
                last_login = set_today(),
                lastname = "Test " + i,
                firstname = "Admin",
                verify_email = true,
            )
//            var done1 = false
//            var done2 = false
//            var done3 = false
//            ref.setValue(user_info).addOnSuccessListener {
//                done1 = true
//                if (done1 && done2 ){
//                    println("debug create $email success")
//                }
//            }
//            val ref2 = database.child("email and account")
//                .child(account)
//            val email_and_account = Email_Account(email = email, account = account)
//            ref2.setValue(email_and_account).addOnSuccessListener {
//                done2 = true
//                if (done1 && done2 ){
//                    println("debug create $email success")
//                }
//            }
//            create_auth_user_firebase(email, password , done1 , done2)
        }
    }

    private fun create_auth_user_firebase(email: String, password: String , done1 : Boolean , done2 : Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this@MainActivity) { task ->
                    if (task.isSuccessful) {
                        var done3 = true
                        if (done1 && done2 && done3){
                            println("debug create $email success")
                        }
                    } else {
                        println("debug create $email fail")
                    }
                }
        }

    }


    private fun signin_user(account: String, password: String) {
        auth.signInWithEmailAndPassword(account, password)
            .addOnSuccessListener {
                set_up_sharePref_and_move_frag(account)
            }.addOnFailureListener {
                progress_signin.visibility = View.INVISIBLE
                tv_signin.visibility = View.VISIBLE
                val s = "Password or Account is incorrect."
                s.showToastShort(this)
            }
    }


    private fun go_to_main_interface() {
        startActivity(Intent(this, Main_Interface_Activity::class.java))
        this.overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        this.finish()
    }

    private fun set_up_sharePref_and_move_frag(maccount_ref: String) {
        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

        var account_ref = ""
        for (i in 0 until maccount_ref.length) {
            if (maccount_ref[i] == '@') {
                break
            }
            account_ref += maccount_ref[i]
        }
        val ref = database
            .child("User")
            .child(account_ref)
            .child("link avatar")
        val ref_name = database
            .child("User")
            .child(account_ref)
            .child("user info")
        var done_avatar = false
        var done_account = false
        var done_name = false
        fun go() {
            if (done_avatar && done_account && done_name) {
                go_to_main_interface()
            }
        }
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                val link_avatar = it.getValue().toString()
                if (sharedPref != null) {
                    with(sharedPref.edit()) {
                        putString("link avatar", link_avatar)
                        apply()
                        done_avatar = true
                        go()
                    }
                }
            } else {
                done_avatar = true
                go()
            }
        }
        ref_name.get().addOnSuccessListener {
            if (it.exists()) {
                val name = it.child("fullname").getValue().toString()
                if (sharedPref != null) {
                    with(sharedPref.edit()) {
                        putString("user name", name)
                        apply()
                        done_name = true
                        go()
                    }
                } else {
                    done_name = true
                    go()
                }
            }
        }
        if (sharedPref != null) {
            with(sharedPref.edit()) {
                putString("account ref", account_ref)
                apply()
                done_account = true
                go()
            }
        } else {
            done_account = true
            go()
        }
    }


    fun showSoftKeyboard(view: View) {
        val inputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }

}