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
import androidx.fragment.app.activityViewModels
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
import theintership.my.`interface`.ICheckWifi
import theintership.my.`interface`.IGetDataFromFirebase
import theintership.my.`interface`.IReplaceFrag
import theintership.my.`interface`.IToast
import theintership.my.databinding.FragSignupNameBinding
import theintership.my.signin_signup.Signup1Activity
import theintership.my.signin_signup.dialog.dialog_loading
import theintership.my.signin_signup.dialog.dialog_stop_signup
import theintership.my.signin_signup.viewModel_Signin_Signup

class Bat : IGetDataFromFirebase {
    override fun onSuccess(str: String) {
    }
}

class frag_signup_name : Fragment(R.layout.frag_signup_name), IReplaceFrag, IToast , ICheckWifi  {

    private var _binding: FragSignupNameBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 1
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signup1Activity: Signup1Activity
    private lateinit var dialogLoading: dialog_loading
    private val viewModel_Signin_Signup: viewModel_Signin_Signup by activityViewModels()

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
                    binding.tvSignupName.text = "Please enter your first and last name"
                if (firstname == "" && lastname != "")
                    binding.tvSignupName.text = "Please enter your firstname"
                if (firstname != "" && lastname == "")
                    binding.tvSignupName.text = "Please enter your lastname"
                return@setOnClickListener
            }

            if (signup1Activity.go_to_frag_signup_age) {
                move_to_frag_age(firstname , lastname)
                return@setOnClickListener
            }

           move_to_frag_birthday(firstname , lastname)

        }

        binding.edtSignupNameLastname.setOnEditorActionListener { textView, i, keyEvent ->
            val firstname = binding.edtSignupNameFistname.text.toString()
            val lastname = binding.edtSignupNameLastname.text.toString()
            if (i == EditorInfo.IME_ACTION_DONE && firstname != "" && lastname != "") {
                if (signup1Activity.go_to_frag_signup_age) {
                   move_to_frag_age(firstname , lastname)
                } else {
                  move_to_frag_birthday(firstname , lastname)
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
        binding.tvSignupName.text = "Please type your real name use in life"
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

    private fun take_fullname_vietnamese(firstName : String , lastName : String) : String{
        return lastName + " " + firstName
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
                showLong("Don't worry , just one thing went wrong. Just enter your name" , signup1Activity)
            }
        } else {
            dialogLoading.dismiss()
            showLong("Don't worry , just one thing went wrong. Just enter your name" , signup1Activity)
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
                                user?.delete()?.addOnCompleteListener{ task2 ->
                                    if (task2.isSuccessful){
                                        dialogLoading.dismiss()
                                    }else{
                                        dialogLoading.dismiss()
                                    }
                                }
                            }
                        } else {
                            dialogLoading.dismiss()
                            show("Connect firebase has problem. Please enter your first and last name", signup1Activity)
                        }
                    }
            }
        } catch (e: Exception) {
            dialogLoading.dismiss()
            show("Connect firebase has problem. Please enter your first and last name", signup1Activity)
        }
    }

    private fun move_to_frag_age(firstName: String , lastName: String){
        replacefrag(
            tag = "frag_signup_age",
            frag = frag_signup_age(),
            fm = signup1Activity.supportFragmentManager
        )
        val fullName = take_fullname_vietnamese(firstName , lastName)
        viewModel_Signin_Signup.set_user_info_fullname(fullname = fullName)
        viewModel_Signin_Signup.set_user_info_firstname(firstname = firstName)
        viewModel_Signin_Signup.set_user_info_lastname(lastname = lastName)
    }

    private fun move_to_frag_birthday(firstName: String , lastName: String){
        replacefrag(
            tag = "frag_signup_birthday",
            frag = frag_signup_birthday(),
            fm = signup1Activity.supportFragmentManager
        )
        val fullName = take_fullname_vietnamese(firstName , lastName)
        viewModel_Signin_Signup.set_user_info_fullname(fullname = fullName)
        viewModel_Signin_Signup.set_user_info_firstname(firstname = firstName)
        viewModel_Signin_Signup.set_user_info_lastname(lastname = lastName)
    }
}