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
import theintership.my.model.limit_auth_phone
import theintership.my.signin_signup.dialog.dialog_loading
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.fragment.frag_done_set_avatar
import theintership.my.signin_signup.fragment.frag_set_avatar
import theintership.my.signin_signup.fragment.frag_signup_name
import theintership.my.signin_signup.shareViewModel


class Signup1Activity : AppCompatActivity() {

    var go_to_frag_signup_age = false
    var signup_with_google = true
    private val shareViewModel: shareViewModel by viewModels()
    private var database: DatabaseReference = Firebase.database.reference
    private lateinit var dialogLoading: dialog_loading

    override fun onDestroy() {
        //See explain in function below
        delete_phone_email_account_of_user()
        super.onDestroy()
    }

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
            update_list_and_limit_auth_phone_and_move_frag()
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

    private fun update_list_and_limit_auth_phone_and_move_frag() {
        dialogLoading.show()
        if (!isWifi(this)) {
            val s = "Please connect wifi to continue"
            s.showToastLong(this)
            dialogLoading.dismiss()
            return
        }
        //Set up list account and phone number and email address , then move to frag_signup_phone
        val myref = database.child("phone and email and account")
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children
                list.forEach {
                    val mphone = it.child("phone").getValue().toString()
                    val memail = it.child("email").getValue().toString()
                    val maccount = it.child("account").getValue().toString()
                    val id = it.child("id").getValue().toString()
                    if (mphone != "") {
                        shareViewModel.list_phone_number.add(mphone)
                    }
                    if (memail != "") {
                        shareViewModel.list_email_address.add(memail)
                    }
                    if (maccount != "") {
                        shareViewModel.list_account.add(maccount)
                    }
                    if (id != "") {
                        shareViewModel.index_of_last_ele_phone_email_account = id.toInt()
                    }
                }
                //Why this function must be here
                //Because firebase function always the last thing program does
                //So i must do what i want to do in firebase function
                set_limit_number_of_auth_phone_in_a_day_and_move_frag()
            }

            override fun onCancelled(error: DatabaseError) {
                dialogLoading.dismiss()
                error_network()
            }

        }
        myref.addValueEventListener(postListener)
    }

    private fun set_limit_number_of_auth_phone_in_a_day_and_move_frag() {
        val today = set_today()
        var day_on_firebase = ""
        var number_of_auth_phone_in_a_day = ""
        val limitAuthPhone = limit_auth_phone(today, 0)

        move_to_frag_name()

        //This will run in behind
        CoroutineScope(Dispatchers.IO).launch {
            val ref_limit_auth_phone = database
                .child("limit_auth_phone_in_a_day")
                .child("1")
            ref_limit_auth_phone.child("day").get().addOnSuccessListener(this@Signup1Activity) {
                day_on_firebase = it.value.toString()
                if (day_on_firebase != today) {
                    ref_limit_auth_phone.setValue(limitAuthPhone)
                }
            }
            ref_limit_auth_phone.child("number").get().addOnSuccessListener(this@Signup1Activity) {
                number_of_auth_phone_in_a_day = it.value.toString()
                if (number_of_auth_phone_in_a_day != "") {
                    shareViewModel.number_of_auth_phone_number_in_a_day =
                        number_of_auth_phone_in_a_day.toInt()
                }
            }
        }
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

        if (frag_last.name == "frag_auth_email_address_account"
            || frag_last.name == "frag_auth_phone_number_account"
        ) {
            if (frag_before_last.name == "frag_auth_phone_number_account" && frag_last.name == "frag_auth_email_address_account") {
                //This case is user entered phone number and email address
                //And user want to authencation email address instead of phone number
                supportFragmentManager.popBackStack()
            } else {
                //if user just entered one of email address or phone number
                //so we can't allow user to back when in authencation
                val s = "Can't back when authencation"
                s.showToastLong(this)
            }
            return
        }

        super.onBackPressed()
    }

    private fun delete_phone_email_account_of_user() {
        //Delete phone and email and account
        val s = "Delete user success"
        val index_of_last_element_phone_email_account =
            shareViewModel.index_of_last_ele_phone_email_account
        if (index_of_last_element_phone_email_account == -1) {
            return
        }
        //This code make frag_auth_phone_number_account crash
        //But the value in ref_phoneEmailAccount1 has been replace after run this code
        //So this code work fine in terms of removeValue from Firebase Realtime Database

        //I must put this into onDestroy because when onDestroy is invoked,
        //frag_auth_phone_number_account has been destroy , so don't need worry about crash fragment
        val ref_phoneEmailAccount1 = database
            .child("phone and email and account")
            .child(index_of_last_element_phone_email_account.toString())

        ref_phoneEmailAccount1
            .removeValue()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    s.showToastLong(this)
                } else {
                    //I don't have any idea to do when it fail
                }
            }
            .addOnFailureListener(this) {
                //I don't have any idea to do when it fail
            }
    }

}


