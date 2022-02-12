package theintership.my.signin_signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import theintership.my.MainActivity
import theintership.my.R
import theintership.my.`interface`.IGetDataFromFirebase
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSignup2Binding

class Bat : IGetDataFromFirebase{
    override fun onSuccess(str: String) {
        println("debug Vao onSuccess ne $str")
    }
}

class frag_signup2 : Fragment(R.layout.frag_signup2), IReplaceFrag, IToast  {

    private var _binding: FragSignup2Binding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 1
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signup1Activity: Signup1Activity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignup2Binding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity
        auth = Firebase.auth
        var check = true
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(signup1Activity, gso)
        Firebase.auth.signOut()
        googleSignInClient.signOut()
        var bat = Bat()
        googleSignIn()

        binding.btnSignup2Go.setOnClickListener {
            val user = Firebase.auth.currentUser
            if (user != null) {
                println("debug user name: ${user.displayName.toString()}")
                show("Co user trong click ${user.displayName.toString()}" , signup1Activity)
            } else {
                println("debug user name khong co")
            }
            val firstname = binding.edtSignup2Fistname.text.toString()
            val lastname = binding.edtSignup2Lastname.text.toString()
            if (firstname == "" || lastname == "") {
                check = false
                binding.layoutSignup2Firstname.isErrorEnabled = true
                binding.layoutSignup2Firstname.error = "ok"
                binding.layoutSignup2Firstname.errorIconDrawable = null

                binding.layoutSignup2Lastname.isErrorEnabled = true
                binding.layoutSignup2Lastname.error = "ok"
                binding.layoutSignup2Lastname.errorIconDrawable = null

                binding.tvSignup2.setTextColor(resources.getColor(R.color.error, null))
                if (firstname == "" && lastname == "")
                    binding.tvSignup2.text = "Vui lòng nhập họ và tên của bạn"
                if (firstname == "" && lastname != "")
                    binding.tvSignup2.text = "Vui lòng nhập tên của bạn"
                if (firstname != "" && lastname == "")
                    binding.tvSignup2.text = "Vui lòng nhập họ của bạn"
                return@setOnClickListener
            }

            if (signup1Activity.go_to_frag_signup3_1) {
                val count = signup1Activity.supportFragmentManager.backStackEntryCount
                show("$count trong frag2", signup1Activity)
                replacefrag(
                    tag = "frag_signup3_1",
                    frag = frag_signup3_1(),
                    fm = signup1Activity.supportFragmentManager
                )
                return@setOnClickListener
            }

            replacefrag(
                tag = "frag_signup3",
                frag = frag_signup3(),
                fm = signup1Activity.supportFragmentManager
            )

        }

        binding.edtSignup2Lastname.setOnEditorActionListener { textView, i, keyEvent ->
            val firstname = binding.edtSignup2Fistname.text.toString()
            val lastname = binding.edtSignup2Lastname.text.toString()
            if (i == EditorInfo.IME_ACTION_DONE && firstname != "" && lastname != "") {
                if (signup1Activity.go_to_frag_signup3_1) {
                    replacefrag(
                        tag = "frag_signup3_1",
                        frag = frag_signup3_1(),
                        fm = signup1Activity.supportFragmentManager
                    )
                } else {
                    replacefrag(
                        tag = "frag_signup3",
                        frag = frag_signup3(),
                        fm = signup1Activity.supportFragmentManager
                    )
                }
                true
            } else {
                false
            }
        }

        binding.edtSignup2Fistname.doAfterTextChanged {
            if (!check) {
                binding.tvSignup2.setTextColor(resources.getColor(R.color.light_grey, null))
                binding.tvSignup2.text = "Nhập tên bạn sử dụng trong đời thực"
                binding.layoutSignup2Firstname.isErrorEnabled = false
                binding.layoutSignup2Lastname.isErrorEnabled = false
                check = true
            }
        }

        binding.edtSignup2Lastname.doAfterTextChanged {
            if (!check) {
                binding.tvSignup2.setTextColor(resources.getColor(R.color.light_grey, null))
                binding.tvSignup2.text = "Nhập tên bạn sử dụng trong đời thực"
                binding.layoutSignup2Firstname.isErrorEnabled = false
                binding.layoutSignup2Lastname.isErrorEnabled = false
                check = true
            }
        }

        binding.btnSignup2Back.setOnClickListener {
            val dialog = dialog(signup1Activity)
            dialog.show()
            dialog.btn_cancel.setOnClickListener {
                startActivity(Intent(signup1Activity, MainActivity::class.java))
                signup1Activity.overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                dialog.dismiss()
            }
        }

        return binding.root
    }

    private fun readData(){


    }

    private fun takelastname(name : String) : String{
        var index = 0
        var lastname = ""
        for (i in 0 until name.length){
            if (name[i] == ' '){
                index = i
            }
        }
        for (i in index + 1 until name.length){
            lastname += name[i]
        }
        return lastname
    }


    private fun takefirstname(name : String) : String{
        var index = 0
        var firstname = ""
        for (i in 0 until name.length){
            if (name[i] == ' '){
                index = i
            }
        }
        for (i in 0 until index){
            firstname += name[i]
        }
        return firstname
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user != null){
            println("debug user co trong start")
        }else{
            println("debug user khong co trong start")
        }
    }

    private fun googleSignIn() {
        println("debug vao google signIn")
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0){
            return
        }
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                firebaseAuthWithGoogle(account , Bat())
            }
            if (account == null){
                println("debug account null trong activity result")
            }
        }else{
            println("debug khac request code")
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount , listener : IGetDataFromFirebase) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try{
                auth.signInWithCredential(credential)
                withContext(Dispatchers.Main){
                    println("debug vao firebaseAuth")
                    listener.onSuccess("Ok trong firebaseAuth")
                }
            } catch (e : Exception){
               println("debug firebaseAuth fail")
            }
        }
    }

}