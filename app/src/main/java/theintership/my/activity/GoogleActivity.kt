package theintership.my.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import theintership.my.R

class GoogleActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google)
        val tv = findViewById<TextView>(R.id.frag_google_text_view)
        auth = Firebase.auth
        val user = auth.currentUser
        // Vì ban đầu mình thiết kế kiểu user đăng kí rồi set model user trên firebase realtime database vào lúc đó luôn,
        // về sau mới thêm tính năng đăng nhập google và facebook. Nếu giờ mình thêm cái code
        // set model thì sẽ rối cho bạn thôi. Nên bạn cứ thêm khảo code đăng nhập google này mà viết lại cũng được.
        // Goodbye.
        if (user != null){
            tv.text = "Google sign in success ${user.email.toString()}, please read comments in code"
        }else{
            tv.text = "Google sign in fail"
        }
    }

}