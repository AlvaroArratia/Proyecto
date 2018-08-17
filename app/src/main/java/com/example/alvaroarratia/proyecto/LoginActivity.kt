package com.example.alvaroarratia.proyecto

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import android.app.ProgressDialog
import android.text.TextUtils
import com.facebook.CallbackManager
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FirebaseUser
import android.support.annotation.NonNull
import android.widget.ProgressBar

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.AuthCredential
import com.facebook.AccessToken
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task


/**
 * Created by Alvaro Arratia on 29-03-2018.
 */
class LoginActivity: AppCompatActivity(), View.OnClickListener {

    private val TAG = "LoginActivity"

    private var eMail: String? = null
    private var pass: String? = null

    private var progressBar: ProgressBar? = null

    private var callbackManager: CallbackManager? = null
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBar = findViewById(R.id.progressBar)

        callbackManager = CallbackManager.Factory.create()

        login_button.setReadPermissions("email")

        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Toast.makeText(applicationContext, R.string.cancel_login, Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(applicationContext, R.string.error_login, Toast.LENGTH_SHORT).show()
            }
        })

        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                goMainScreen()
            }
        }

        initialise()
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken) {
        progressBar!!.setVisibility(View.VISIBLE)
        login_button.setVisibility(View.GONE)
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        mAuth!!.signInWithCredential(credential).addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
            override fun onComplete(task: Task<AuthResult>) {
                if (!task.isSuccessful()) {
                    Toast.makeText(applicationContext, R.string.firebase_error_login, Toast.LENGTH_LONG).show()
                }
                progressBar!!.setVisibility(View.GONE)
                login_button.setVisibility(View.VISIBLE)
            }
        })
    }

    private fun goMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        mAuth!!.removeAuthStateListener(mAuthListener!!)
    }

    override fun onClick(view: View?) {
        val i = view!!.id
        when (i) {
            R.id.btn_login -> {
                loginUser()
            }
            R.id.btn_crearCuenta -> {
                val intent = Intent(this, CrearCuentaActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun initialise() {
        mAuth = FirebaseAuth.getInstance()
        btn_crearCuenta.setOnClickListener(this)
        btn_login.setOnClickListener(this)
    }

    private fun loginUser() {
        eMail = txtEmail.text.toString()
        pass = txtPass.text.toString()
        if (!TextUtils.isEmpty(eMail) && !TextUtils.isEmpty(pass)) {
            progressBar!!.setVisibility(View.VISIBLE)
            Log.d(TAG, "Logging in user.")
            mAuth!!.signInWithEmailAndPassword(eMail!!, pass!!)
                    .addOnCompleteListener(this) { task ->
                        progressBar!!.setVisibility(View.GONE)
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithEmail:success")
                            updateUI()
                        } else {
                            Log.e(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(this, "Fallo en la autenticacion.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        } else {
            Toast.makeText(this, "LLenar todos los campos.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

}