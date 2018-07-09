package com.example.alvaroarratia.proyecto

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.database.DatabaseReference
import java.util.*

class CrearActivity : AppCompatActivity() , View.OnClickListener {

    private var mDatabase: DatabaseReference? = null
    private var mMessageReferencia: DatabaseReference? = null
    private var eventos: ArrayList<Evento>? = null

    private val TAG = "ChatActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(v: View?) {

    }

    /**
     * Envía mensaje a base de datos firebase
     */
    private fun enviarEvento(titulo: String, nombre: String, hora: String) {
        val msj = Evento(titulo, nombre, hora)
        mMessageReferencia!!.push().setValue(msj)
    }

    private fun getHora(): String {
        var horas: String
        var min: String
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
        return horas + ":" + min
    }

}