package theintership.my.signin_signup

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.loader.content.AsyncTaskLoader
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.signin_signup.dialog.dialog_delete_account
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.fragment.frag_signup_creating_account
import theintership.my.signin_signup.fragment.frag_signup_name
import java.net.URL


class Signup1Activity : AppCompatActivity(), IReplaceFrag, IToast {

    var go_to_frag_signup_age = false
    var signup_with_google = true
    var check_create_user_once_time = true
    private var database: DatabaseReference = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btnGo = findViewById<TextView>(R.id.btn_signup1_go)
        val btnShowDialog = findViewById<TextView>(R.id.btn_signup1_showdialog)
        val btnBack = findViewById<ImageView>(R.id.btn_signup1_back)



        btnGo.setOnClickListener {
            replacefrag(
                tag = "frag_signup_name",
                frag = frag_signup_name(),
                fm = supportFragmentManager
            )
        }

        btnShowDialog.setOnClickListener {
            val dialog = dialog_stop_signup(this)
            dialog.show()
            dialog.btn_cancel.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                dialog.dismiss()
            }
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private fun get_email_for_ref(email: String): String {
        var ans = ""
        for (i in 0 until email.length) {
            if (email[i] == '@') {
                break
            }
            ans += email[i]
        }
        return ans
    }

    override fun onBackPressed() {
        val size = supportFragmentManager.backStackEntryCount
        if (size == 1) {
            //User is in frag_signup_name
            val dialog = dialog_stop_signup(this)
            dialog.show()
            dialog.btn_cancel.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                dialog.dismiss()
            }
            return
        }
        if (size == 0) {
            //User want to return to signin , and sign in is in MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            return
        }
        val frag_last = supportFragmentManager.getBackStackEntryAt(size - 1)
        val frag_before_last = supportFragmentManager.getBackStackEntryAt(size - 2)

        if (frag_last.name == "frag_signup_age" && frag_before_last.name == "frag_signup_birthday") {
            //User want to rename so must pop 2 fragment
            supportFragmentManager.popBackStack()
            supportFragmentManager.popBackStack()
            return
        }

        if (frag_last.name == "frag_signup_age" && frag_before_last.name == "frag_signup_name") {
            //User go to frag_signup_age from frag_signup_name so just need pop 1 fragment
            supportFragmentManager.popBackStack()
            return
        }

        if (frag_last.name == "frag_signup_creating_account" || frag_last.name == "frag_signing_account") {
            //Don't let user pop back when program is creating account or signing account
            return
        }

        super.onBackPressed()
    }


}