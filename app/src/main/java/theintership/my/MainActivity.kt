package theintership.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import theintership.my.`interface`.IReplaceFrag
import theintership.my.signin_signup.frag_sigin

class MainActivity : AppCompatActivity() , IReplaceFrag{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replacefrag(tag = "frag_signin" , frag = frag_sigin() , fm = supportFragmentManager)

    }

}