package theintership.my.signin_signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
import theintership.my.databinding.FragSignupNameBinding

class Bat : IGetDataFromFirebase {
    override fun onSuccess(str: String) {
        val lastname = frag_signup2().takelastname(str)
    }
}


class frag_signup2 : Fragment(R.layout.frag_signup_name), IReplaceFrag, IToast {

    private var _binding: FragSignupNameBinding? = null
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
        _binding = FragSignupNameBinding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity
        signup1Activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        auth = Firebase.auth
        var check = true
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(signup1Activity, gso)

        Firebase.auth.signOut()
        googleSignInClient.signOut()
        googleSignIn()

        binding.btnSignupNameGo.setOnClickListener {
            val firstname = binding.edtSignupNameFistname.text.toString()
            val lastname = binding.edtSignupNameLastname.text.toString()
            if (firstname == "" || lastname == "") {
                check = false
                binding.layoutSignupNameFirstname.isErrorEnabled = true
                binding.layoutSignupNameFirstname.error = "ok"
                binding.layoutSignupNameFirstname.errorIconDrawable = null

                binding.layoutSignupNameLastname.isErrorEnabled = true
                binding.layoutSignupNameLastname.error = "ok"
                binding.layoutSignupNameLastname.errorIconDrawable = null

                binding.tvSignupName.setTextColor(resources.getColor(R.color.error, null))
                if (firstname == "" && lastname == "")
                    binding.tvSignupName.text = "Vui lòng nhập họ và tên của bạn"
                if (firstname == "" && lastname != "")
                    binding.tvSignupName.text = "Vui lòng nhập tên của bạn"
                if (firstname != "" && lastname == "")
                    binding.tvSignupName.text = "Vui lòng nhập họ của bạn"
                return@setOnClickListener
            }

            if (signup1Activity.go_to_frag_signup3_1) {
                val count = signup1Activity.supportFragmentManager.backStackEntryCount
                show("$count trong frag2", signup1Activity)
                replacefrag(
                    tag = "frag_signup3_1",
                    frag = frag_signup_age(),
                    fm = signup1Activity.supportFragmentManager
                )
                return@setOnClickListener
            }

            replacefrag(
                tag = "frag_signup3",
                frag = frag_signup_birthday(),
                fm = signup1Activity.supportFragmentManager
            )

        }

        binding.edtSignupNameLastname.setOnEditorActionListener { textView, i, keyEvent ->
            val firstname = binding.edtSignupNameFistname.text.toString()
            val lastname = binding.edtSignupNameLastname.text.toString()
            if (i == EditorInfo.IME_ACTION_DONE && firstname != "" && lastname != "") {
                if (signup1Activity.go_to_frag_signup3_1) {
                    replacefrag(
                        tag = "frag_signup3_1",
                        frag = frag_signup_age(),
                        fm = signup1Activity.supportFragmentManager
                    )
                } else {
                    replacefrag(
                        tag = "frag_signup3",
                        frag = frag_signup_birthday(),
                        fm = signup1Activity.supportFragmentManager
                    )
                }
                true
            } else {
                binding.layoutSignupNameFirstname.isErrorEnabled = true
                binding.layoutSignupNameFirstname.error = "ok"
                binding.layoutSignupNameFirstname.errorIconDrawable = null

                binding.layoutSignupNameLastname.isErrorEnabled = true
                binding.layoutSignupNameLastname.error = "ok"
                binding.layoutSignupNameLastname.errorIconDrawable = null
                false
            }
        }

        binding.edtSignupNameFistname.doAfterTextChanged {
            if (!check) {
                binding.tvSignupName.setTextColor(resources.getColor(R.color.light_grey, null))
                binding.tvSignupName.text = "Nhập tên bạn sử dụng trong đời thực"
                binding.layoutSignupNameFirstname.isErrorEnabled = false
                binding.layoutSignupNameLastname.isErrorEnabled = false
                check = true
            }
        }

        binding.edtSignupNameLastname.doAfterTextChanged {
            if (!check) {
                binding.tvSignupName.setTextColor(resources.getColor(R.color.light_grey, null))
                binding.tvSignupName.text = "Nhập tên bạn sử dụng trong đời thực"
                binding.layoutSignupNameFirstname.isErrorEnabled = false
                binding.layoutSignupNameLastname.isErrorEnabled = false
                check = true
            }
        }

        binding.btnSignupNameBack.setOnClickListener {
            val dialog = dialog_cancel_signup(signup1Activity)
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


    fun takelastname(name: String): String {
        var index = 0
        var lastname = ""
        for (i in 0 until name.length) {
            if (name[i] == ' ') {
                index = i
            }
        }
        for (i in index + 1 until name.length) {
            lastname += name[i]
        }
        return lastname
    }


    private fun takefirstname(name: String): String {
        var index = 0
        var firstname = ""
        for (i in 0 until name.length) {
            if (name[i] == ' ') {
                index = i
            }
        }
        for (i in 0 until index) {
            firstname += name[i]
        }
        return firstname
    }


    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != 0) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
                account?.let {
                    firebaseAuthWithGoogle(account)
                }
            } else {
            }
        }
    }

    private fun firebaseAuthWithGoogle(
        account: GoogleSignInAccount,
    ) {
        var a = 1
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        try {
            CoroutineScope(Dispatchers.IO).launch {
                auth.signInWithCredential(credential)
            }
        } catch (e: Exception) {
            show("Google login fail , pls try again", signup1Activity)
            a = 2
        }
        while (Firebase.auth.currentUser != null || a == 1) {
            if (Firebase.auth.currentUser != null) {
                val user = Firebase.auth.currentUser
                val name = user?.displayName.toString()
                binding.edtSignupNameLastname.setText(takelastname(name))
                binding.edtSignupNameFistname.setText(takefirstname(name))
                break
            }
        }
    }

}