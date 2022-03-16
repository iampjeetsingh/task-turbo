package `in`.thesupremeone.taskturbo.activities

import `in`.thesupremeone.taskturbo.R
import `in`.thesupremeone.taskturbo.databinding.ActivityLoginBinding
import `in`.thesupremeone.taskturbo.viewmodels.LoginViewModel
import `in`.thesupremeone.taskturbo.viewmodels.LoginViewModelFactory
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel
    val RC_SIGN_IN: Int = 123
    val TAG = "LoginActivity"
    private lateinit var auth: FirebaseAuth
    var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // Initialize Firebase Auth
        auth = Firebase.auth

        viewModel = ViewModelProvider(this, LoginViewModelFactory(auth))[LoginViewModel::class.java]

        // Configure Google Sign In inside onCreate method
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("201161370801-kvhl1ltbaiah0j921bv31m8t8bubvgf4.apps.googleusercontent.com")
            .requestEmail()
            .build()
        // getting the value of gso inside the GoogleSigninClient
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.revokeAccess()


        binding.Signin.setOnClickListener{
            signIn()
        }

        viewModel.user.observe(this){
            if(it!=null){
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        viewModel.loading.observe(this){
            if(it){
                progressDialog = ProgressDialog(this);
                progressDialog!!.setMessage("Loading...")
                progressDialog!!.setCancelable(false)
                progressDialog!!.show()
            }else{
                if(progressDialog!=null && progressDialog!!.isShowing){
                    progressDialog!!.dismiss()
                }
                progressDialog = null
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        viewModel.postUser(auth.currentUser)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        viewModel.setLoading(true)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                viewModel.firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                viewModel.setLoading(false)
            }
        }
    }
}