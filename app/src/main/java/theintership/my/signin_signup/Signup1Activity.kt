package theintership.my.signin_signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IReplaceFrag

class Signup1Activity : AppCompatActivity(), IReplaceFrag {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btnGo = findViewById<TextView>(R.id.btn_signup1_go)
        val btnShowDialog = findViewById<TextView>(R.id.btn_signup1_showdialog)
        val btnBack = findViewById<ImageView>(R.id.btn_signup1_back)


        btnGo.setOnClickListener {
            replacefrag(tag = "frag_signup2", frag = frag_signup2(), fm = supportFragmentManager)
        }

        btnShowDialog.setOnClickListener {

        }

        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }


}