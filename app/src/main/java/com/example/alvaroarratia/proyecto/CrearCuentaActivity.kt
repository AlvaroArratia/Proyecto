package com.example.alvaroarratia.proyecto

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_crearcuenta.*

class CrearCuentaActivity: AppCompatActivity(), View.OnClickListener {

    private val TAG = "CreateAccountActivity"

    private var name: String? = null
    private var eMail: String? = null
    private var pass: String? = null

    private var mProgressBar: ProgressBar? = null

    //private var mDatabaseReference: DatabaseReference? = null
    //private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crearcuenta)

        mProgressBar = findViewById(R.id.progressBar2)

        mAuth = FirebaseAuth.getInstance()

        btn_enviarCuenta.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val i = view!!.id
        when (i) {
            R.id.btn_enviarCuenta -> {
                createNewAccount()
            }
            else -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createNewAccount() {
        name = txtNombreUser?.text.toString()
        eMail = txtCorreo?.text.toString()
        pass = txtContraseña?.text.toString()
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(eMail) && !TextUtils.isEmpty(pass)) {
            mProgressBar!!.setVisibility(View.VISIBLE)
            mAuth!!.createUserWithEmailAndPassword(eMail!!, pass!!)
                    .addOnCompleteListener(this) { task ->
                        mProgressBar!!.setVisibility(View.VISIBLE)
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            /*val userId = mAuth!!.currentUser!!.uid
                            val currentUserDb = mDatabaseReference!!.child(userId)
                            currentUserDb.child("name").setValue(name)*/
                            updateUserInfoAndUI()
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(this, "Fallo en la autenticacion.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        } else {
            Toast.makeText(this, "Llenar todos los campos.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserInfoAndUI() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}