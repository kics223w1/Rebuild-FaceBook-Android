package theintership.my.signin_signup.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.BaseAdapter
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
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_loading
import theintership.my.signin_signup.dialog.dialog_stop_signup

class Bat : IGetDataFromFirebase {
    override fun onSuccess(str: String) {
    }
}

class frag_signup_name : Fragment(R.layout.frag_signup_name), IReplaceFrag, IToast {

    private var _binding: FragSignupNameBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 1
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signup1Activity: Signup1Activity
    private lateinit var dialogLoading: dialog_loading


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragSignupNameBinding.inflate(inflater, container, false)
        signup1Activity = activity as Signup1Activity
        signup1Activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        auth = Firebase.auth
        dialogLoading = dialog_loading(signup1Activity)
        var check_name_empty = false
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(signup1Activity, gso)

        if (signup1Activity.signup_with_google) {
            //With this condition ,google sign in just show one times
            //If user return to this fragment from other fragment in signup
            //google sign in will not show
            dialogLoading.show()
            Firebase.auth.signOut()
            googleSignInClient.signOut()
            googleSignIn()
            signup1Activity.signup_with_google = false
        }

        binding.btnSignupNameGo.setOnClickListener {
            val firstname = binding.edtSignupNameFistname.text.toString()
            val lastname = binding.edtSignupNameLastname.text.toString()
            if (firstname == "" || lastname == "") {
                check_name_empty = true
                set_error_edittext()

                if (firstname == "" && lastname == "")
                    binding.tvSignupName.text = "Vui lòng nhập họ và tên của bạn"
                if (firstname == "" && lastname != "")
                    binding.tvSignupName.text = "Vui lòng nhập tên của bạn"
                if (firstname != "" && lastname == "")
                    binding.tvSignupName.text = "Vui lòng nhập họ của bạn"
                return@setOnClickListener
            }

            if (signup1Activity.go_to_frag_signup_age) {
                replacefrag(
                    tag = "frag_signup_age",
                    frag = frag_signup_age(),
                    fm = signup1Activity.supportFragmentManager
                )
                return@setOnClickListener
            }

            replacefrag(
                tag = "frag_signup_birthday",
                frag = frag_signup_birthday(),
                fm = signup1Activity.supportFragmentManager
            )

        }

        binding.edtSignupNameLastname.setOnEditorActionListener { textView, i, keyEvent ->
            val firstname = binding.edtSignupNameFistname.text.toString()
            val lastname = binding.edtSignupNameLastname.text.toString()
            if (i == EditorInfo.IME_ACTION_DONE && firstname != "" && lastname != "") {
                if (signup1Activity.go_to_frag_signup_age) {
                    replacefrag(
                        tag = "frag_signup_age",
                        frag = frag_signup_age(),
                        fm = signup1Activity.supportFragmentManager
                    )
                } else {
                    replacefrag(
                        tag = "frag_signup_birthday",
                        frag = frag_signup_birthday(),
                        fm = signup1Activity.supportFragmentManager
                    )
                }
                true
            } else {
                set_error_edittext()
                false
            }
        }

        binding.edtSignupNameFistname.doAfterTextChanged {
            if (check_name_empty) {
                move_error_edittext()
                check_name_empty = false
            }
        }

        binding.edtSignupNameLastname.doAfterTextChanged {
            if (check_name_empty) {
                move_error_edittext()
                check_name_empty = false
            }
        }

        binding.btnSignupNameBack.setOnClickListener {
            val dialog = dialog_stop_signup(signup1Activity)
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


    private fun move_error_edittext() {
        binding.tvSignupName.setTextColor(resources.getColor(R.color.light_grey, null))
        binding.tvSignupName.text = "Nhập tên bạn sử dụng trong đời thực"
        binding.layoutSignupNameFirstname.isErrorEnabled = false
        binding.layoutSignupNameLastname.isErrorEnabled = false
    }

    private fun set_error_edittext() {
        binding.layoutSignupNameFirstname.isErrorEnabled = true
        binding.layoutSignupNameFirstname.error = "ok"
        binding.layoutSignupNameFirstname.errorIconDrawable = null

        binding.layoutSignupNameLastname.isErrorEnabled = true
        binding.layoutSignupNameLastname.error = "ok"
        binding.layoutSignupNameLastname.errorIconDrawable = null

        binding.tvSignupName.setTextColor(resources.getColor(R.color.error, null))
    }

    private fun takelastname(name: String): String {
        var index = -1
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
        var index = -1
        var firstname = ""
        for (i in 0 until name.length) {
            if (name[i] == ' ') {
                index = i
            }
        }
        if (index == -1) {
            index = name.length
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
                dialogLoading.dismiss()
            }
        } else {
            dialogLoading.dismiss()
        }
    }

    private fun firebaseAuthWithGoogle(
        account: GoogleSignInAccount,
    ) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        try {
            CoroutineScope(Dispatchers.IO).launch {
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(signup1Activity) { task ->
                        if (task.isSuccessful) {
                            if (Firebase.auth.currentUser != null) {
                                val user = Firebase.auth.currentUser
                                val name = user?.displayName.toString()
                                binding.edtSignupNameLastname.setText(takelastname(name))
                                binding.edtSignupNameFistname.setText(takefirstname(name))
                                Firebase.auth.signOut()
                                googleSignInClient.signOut()
                                dialogLoading.dismiss()
                            }
                        } else {
                            dialogLoading.dismiss()
                            show("debug Log in fail", signup1Activity)
                        }
                    }
            }
        } catch (e: Exception) {
            show("Google login fail , pls try again", signup1Activity)
        }
    }
}