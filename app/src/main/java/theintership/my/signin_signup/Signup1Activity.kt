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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.ICheckWifi
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.signin_signup.dialog.dialog_delete_account
import theintership.my.signin_signup.dialog.dialog_loading
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.fragment.frag_signup_creating_account
import theintership.my.signin_signup.fragment.frag_signup_name
import theintership.my.signin_signup.fragment.frag_signup_phone
import java.net.URL


class Signup1Activity : AppCompatActivity(), IReplaceFrag, IToast , ICheckWifi {

    var go_to_frag_signup_age = false
    var signup_with_google = true
    var check_create_user_once_time = true
    private val viewModel_Signin_Signup = viewModel_Signin_Signup()
    private var database: DatabaseReference = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btnGo = findViewById<TextView>(R.id.btn_signup1_go)
        val btnShowDialog = findViewById<TextView>(R.id.btn_signup1_showdialog)
        val btnBack = findViewById<ImageView>(R.id.btn_signup1_back)



        btnGo.setOnClickListener {
            move_to_frag_name()
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

    private fun move_to_frag_name(){
        val dialogLoading = dialog_loading(this)
        dialogLoading.show()
        if (!isWifi(this)){
            showLong("Please connect wifi to continue" , this)
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
                    if(mphone != ""){
                        viewModel_Signin_Signup.list_phone_number.add(mphone)
                    }
                    if (memail != ""){
                        viewModel_Signin_Signup.list_email_address.add(memail)
                    }
                    if (maccount != ""){
                        viewModel_Signin_Signup.list_account.add(maccount)
                    }
                }
                dialogLoading.dismiss()
                //Why this function must be here
                //Because firebase function always the last thing program does
                //So i must do what i want to do in firebase function
                replacefrag(
                    tag = "frag_signup_name",
                    frag = frag_signup_name(),
                    fm = supportFragmentManager
                )
            }

            override fun onCancelled(error: DatabaseError) {
                dialogLoading.dismiss()
                replacefrag(
                    tag = "frag_signup_name",
                    frag = frag_signup_name(),
                    fm = supportFragmentManager
                )
            }

        }
        myref.addValueEventListener(postListener)
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
                show("Can't back when creating account" , this)
            return
        }

        super.onBackPressed()
    }


}