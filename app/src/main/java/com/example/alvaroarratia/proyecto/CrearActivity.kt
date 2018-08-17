package com.example.alvaroarratia.proyecto

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_crear.*
import java.util.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class CrearActivity : AppCompatActivity(), View.OnClickListener {

    private var mDatabase: DatabaseReference? = null
    private var mMessageReferencia: DatabaseReference? = null
    private var cat: String? = null
    private var spinner: Spinner? = null
    private var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear)

        mDatabase = FirebaseDatabase.getInstance().reference
        mMessageReferencia = FirebaseDatabase.getInstance().getReference("eventos")

        userEmail = intent.getStringExtra("emailUser")
        Log.i("email", userEmail)

        spinner = findViewById<View>(R.id.spinner_cat) as Spinner
        val categorias = arrayOf("Honor", "Primera", "Segunda", "Tercera", "Cuarta")
        spinner!!.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)

        spinner()
        btn_volver.setOnClickListener(this)
        btn_crear.setOnClickListener(this)
    }

    fun spinner() {
        spinner!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, pos: Int, id: Long) {
                cat = adapterView.getItemAtPosition(pos) as String
                Log.i("SpinnerItem", cat)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.i("SpinnerItem", cat)
            }
        }
    }

    override fun onClick(view: View?) {
        val i = view!!.id
        when (i) {
            R.id.btn_crear -> {
                enviarEvento(txtNombreEvento.text.toString(), txtNombreUsuario.text.toString(), getHora(),
                        cat!!, txtHoraDesde.text.toString(), txtHoraHasta.text.toString(), userEmail!!)
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            R.id.btn_volver -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            else -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun enviarEvento(titulo: String, nombre: String, hora: String, categoria: String,
                             horaInicio: String, horaFin: String, eMail: String) {
        val ev = Evento(titulo, nombre, hora, categoria, horaInicio, horaFin, eMail)
        mMessageReferencia!!.push().setValue(ev)
    }

    private fun getHora(): String {
        val horas: String
        val min: String
        val dia: String
        val mes: String
        val año: String

        if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 10) {
            dia = "0" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
        } else {
            dia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
        }
        if (Calendar.getInstance().get(Calendar.MONTH) < 10) {
            mes = "0" + Calendar.getInstance().get(Calendar.MONTH).toString()
        } else {
            mes = Calendar.getInstance().get(Calendar.MONTH).toString()
        }
        año = Calendar.getInstance().get(Calendar.YEAR).toString()

        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 10) {
            horas = "0" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString()
        } else {
            horas = Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString()
        }
        if (Calendar.getInstance().get(Calendar.MINUTE) < 10) {
            min = "0" + Calendar.getInstance().get(Calendar.MINUTE).toString()
        } else {
            min = Calendar.getInstance().get(Calendar.MINUTE).toString()
        }
        return dia + "/" + mes + "/" + año + "\n" + horas + ":" + min
    }

}