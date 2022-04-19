package theintership.my

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import theintership.my.all_class.MyMethod.Companion.check_wifi
import theintership.my.all_class.MyMethod.Companion.isWifi
import theintership.my.all_class.MyMethod.Companion.replacefrag
import theintership.my.all_class.MyMethod.Companion.set_today
import theintership.my.all_class.MyMethod.Companion.showToastLong
import theintership.my.all_class.MyMethod.Companion.showToastShort
import theintership.my.signin_signup.model.limit_auth_phone
import theintership.my.signin_signup.dialog.dialog_loading
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.fragment.frag_done_set_avatar
import theintership.my.signin_signup.fragment.frag_set_avatar
import theintership.my.signin_signup.fragment.frag_show_image_for_chosing_avatar
import theintership.my.signin_signup.fragment.frag_signup_name
import theintership.my.signin_signup.shareViewModel


class Signup1Activity : AppCompatActivity() {

    var go_to_frag_signup_age = false
    var signup_with_google = true
    private val shareViewModel: shareViewModel by viewModels()
    private var database: DatabaseReference = Firebase.database.reference
    private lateinit var dialogLoading: dialog_loading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btnSignup = findViewById<TextView>(R.id.btn_signup1_go)
        val btnShowDialog = findViewById<TextView>(R.id.btn_signup1_showdialog)
        val btnBack = findViewById<ImageView>(R.id.btn_signup1_back)

        dialogLoading = dialog_loading(this)

        btnSignup.setOnClickListener {
            if (!check_wifi(this)) {
                return@setOnClickListener
            }
            update_list_email_and_move_frag()
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

    private fun update_list_email_and_move_frag() {
        dialogLoading.show()
        if (!isWifi(this)) {
            val s = "Please connect wifi to continue"
            s.showToastLong(this)
            dialogLoading.dismiss()
            return
        }
        //Set up list account and phone number and email address , then move to frag_signup_phone
        val myref = database.child("email and account")
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children
                list.forEach {
                    val memail = it.child("email").getValue().toString()
                    val maccount = it.child("account").getValue().toString()
                    val id = it.child("id").getValue().toString()
                    if (memail != "") {
                        shareViewModel.list_email_address.add(memail)
                    }
                    if (maccount != "") {
                        shareViewModel.list_account.add(maccount)
                    }
                    if (id != "") {
                        shareViewModel.index_of_last_ele_email_account = id.toInt()
                    }
                }
                println("debug list email: ${shareViewModel.list_email_address}")
                println("debug list account: ${shareViewModel.list_account}")
                move_to_frag_name()
            }

            override fun onCancelled(error: DatabaseError) {
                dialogLoading.dismiss()
                error_network()
            }

        }
        myref.addValueEventListener(postListener)
    }

    private fun error_network() {
        if (!isWifi(this)) {
            val s = "Please check your wifi connection and click next again."
            s.showToastLong(this)
        } else {
            val s = "Our sever went wrong. Sorry for the error. Please click next again."
            s.showToastShort(this)
        }
    }

    private fun move_to_frag_name() {
        dialogLoading.dismiss()
//        replacefrag(
//            tag = "frag_set_avatar",
//            frag = frag_set_avatar(),
//            fm = supportFragmentManager
//        )


        replacefrag(
            tag = "frag_signup_name",
            frag = frag_signup_name(),
            fm = supportFragmentManager
        )
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
                this.finish()
                dialog.dismiss()
            }
            return
        }
        if (size == 0) {
            //User want to return to signin , and sign in is in MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            this.finish()
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
            var s = "Can't back when creating account"
            if (frag_last.name == "frag_signing_account") {
                s = "Can't back when signing account"
            }
            s.showToastLong(this)
            return
        }

        if (frag_last.name == "frag_auth_email_address_account") {
            val s = "Can't back when authencation"
            s.showToastLong(this)
            return
        }

        super.onBackPressed()
    }


}


