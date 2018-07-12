package com.example.alvaroarratia.proyecto

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.example.alvaroarratia.proyecto.R.layout.activity_main
import com.example.alvaroarratia.proyecto.R.layout.evento_layout
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.evento_layout.*
import java.util.*

class MainActivity : AppCompatActivity() , View.OnClickListener {

    private var mDatabase: DatabaseReference? = null
    private var mMessageReferencia: DatabaseReference? = null
    private var eventos: ArrayList<Evento>? = null

    private val TAG = "ChatActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDatabase = FirebaseDatabase.getInstance().reference
        mMessageReferencia = FirebaseDatabase.getInstance().getReference("eventos")

        //relativeLayout.setOnClickListener(this)

        escucharEvento()

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        eventos = ArrayList<Evento>()

        var adapter = EventoAdapter(eventos!!)
        recyclerView.adapter = adapter

    }

    override fun onClick(view: View?) {
        val i = view!!.id
        when (i) {
            R.id.relativeLayout -> {
                val intent = Intent(this, CrearActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun escucharEvento() {
        val escuchadorEventos = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val msjData = data.getValue<Evento>(Evento::class.java)
                    val msj = msjData?.let { it } ?: continue

                    eventos!!.add(msj)
                    Log.e(TAG, "onDataChange: Message data is updated: " + msj!!.toString())
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.i(TAG, "Error al escuchar mensajes")
            }
        }
        mMessageReferencia!!.addValueEventListener(escuchadorEventos)
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
