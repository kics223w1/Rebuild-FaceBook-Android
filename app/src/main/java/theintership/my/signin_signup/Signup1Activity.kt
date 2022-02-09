package theintership.my.signin_signup

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast

class Signup1Activity : AppCompatActivity(), IReplaceFrag, IToast {

    var go_to_frag_signup3_1 = false
    var PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btnGo = findViewById<TextView>(R.id.btn_signup1_go)
        val btnShowDialog = findViewById<TextView>(R.id.btn_signup1_showdialog)
        val btnBack = findViewById<ImageView>(R.id.btn_signup1_back)

        if (!checkpermission("android.permission.GET_ACCOUNTS")) {
            requestpermission("android.permission.GET_ACCOUNTS")
        } else {
            getEmails()
        }


        btnGo.setOnClickListener {
            replacefrag(tag = "frag_signup2", frag = frag_signup2(), fm = supportFragmentManager)
        }

        btnShowDialog.setOnClickListener {
            val dialog = dialog(this)
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

    override fun onBackPressed() {
        val size = supportFragmentManager.backStackEntryCount
        if (size == 1) {
            val dialog = dialog(this)
            dialog.show()
            dialog.btn_cancel.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                dialog.dismiss()
            }
            return
        }
        if (size == 0) {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            return
        }
        val frag_last = supportFragmentManager.getBackStackEntryAt(size - 1)
        val frag_before_last = supportFragmentManager.getBackStackEntryAt(size - 2)
        if (frag_last.name == "frag_signup3_1" && frag_before_last.name == "frag_signup3") {
            supportFragmentManager.popBackStack()
            supportFragmentManager.popBackStack()
            return
        }
        if (frag_last.name == "frag_signup3_1" && frag_before_last.name != "frag_signup3") {
            supportFragmentManager.popBackStack()
            return
        }
        super.onBackPressed()
    }

    fun getEmails() {
        val manager: AccountManager = AccountManager.get(this)
        val list = manager.getAccountsByType("com.google")
        println("debug ${list.size}")
        for (i in list){
            println("debug ${i.name}")
        }

    }

    fun checkpermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return true
    }

    fun requestpermission(permission: String) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            show("Allow di", this)
        }
        val list: Array<String?> = arrayOf(permission)
        ActivityCompat.requestPermissions(this, list, PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getEmails()
            } else {
                show("Khong co request code", this)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}