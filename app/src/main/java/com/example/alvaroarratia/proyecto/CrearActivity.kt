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

class CrearActivity : AppCompatActivity() , View.OnClickListener {

    private var mDatabase: DatabaseReference? = null
    private var mMessageReferencia: DatabaseReference? = null

    private val TAG = "ChatActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear)

        mDatabase = FirebaseDatabase.getInstance().reference
        mMessageReferencia = FirebaseDatabase.getInstance().getReference("eventos")

        btn_crear.setOnClickListener(this)
        btn_volver.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val i = view!!.id
        when (i) {
            R.id.btn_crear-> {
                enviarEvento(txtAlgo.text.toString(), txtAlgo2.text.toString(), getHora())
                //Log.e(TAG,"No escribe datos")
                txtAlgo.setText("")
                txtAlgo2.setText("")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_volver -> {
                txtAlgo.setText("")
                txtAlgo2.setText("")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /**
     * Envía eventos a base de datos firebase
     */
    private fun enviarEvento(titulo: String, nombre: String, hora: String) {
        val msj = Evento(titulo, nombre, hora)
        mMessageReferencia!!.push().setValue(msj)
    }

    private fun getHora(): String {
        var horas: String
        var min: String
        var dia: String
        var mes: String
        var año: String
        dia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
        mes = Calendar.getInstance().get(Calendar.MONTH).toString()
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
        return dia + "-" + mes + "-" + año + " / " + horas + ":" + min
    }

}