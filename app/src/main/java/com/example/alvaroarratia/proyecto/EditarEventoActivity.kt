package com.example.alvaroarratia.proyecto

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_crear.*
import java.util.*

class EditarEventoActivity: AppCompatActivity(), View.OnClickListener {

    private var eventoAtributos: ArrayList<String>? = null

    private var mDatabase: DatabaseReference? = null
    private var mMessageReferencia: DatabaseReference? = null

    private var cat: String? = null
    private var spinner: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear)

        btn_crear.text = "Editar Evento"

        mDatabase = FirebaseDatabase.getInstance().reference
        mMessageReferencia = FirebaseDatabase.getInstance().getReference("eventos")

        eventoAtributos = ArrayList()
        eventoAtributos = intent.getStringArrayListExtra("eventoSeleccionado")

        spinner = findViewById<View>(R.id.spinner_cat) as Spinner
        val categorias = arrayOf("Honor", "Primera", "Segunda", "Tercera", "Cuarta")
        spinner!!.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)

        spinner()
        btn_volver.setOnClickListener(this)
        btn_crear.setOnClickListener(this)
    }

    fun spinner() {
        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

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
                editarEvento()
            }
            R.id.btn_volver -> {
                val intent = Intent(this, EventoCreadorActivity::class.java)
                intent.putExtra("eventoSeleccionado", eventoAtributos)
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
        val intent = Intent(this, EventoCreadorActivity::class.java)
        intent.putExtra("eventoSeleccionado", eventoAtributos)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun editarEvento() {
        val nombreEvento: String
        val nombreUsuario: String
        val horaInicio: String
        val horaFin: String

        if (txtNombreEvento.text.toString().equals("")) {
            nombreEvento = eventoAtributos!![0]
        } else {
            nombreEvento = txtNombreEvento.text.toString()
        }
        if (txtNombreUsuario.text.toString().equals("")) {
            nombreUsuario = eventoAtributos!![1]
        } else {
            nombreUsuario = txtNombreUsuario.text.toString()
        }
        if (txtHoraDesde.text.toString().equals("")) {
            horaInicio = eventoAtributos!![3]
        } else {
            horaInicio = txtHoraDesde.text.toString()
        }
        if (txtHoraHasta.text.toString().equals("")) {
            horaFin = eventoAtributos!![4]
        } else {
            horaFin = txtHoraHasta.text.toString()
        }
        enviarEvento(nombreEvento, nombreUsuario, eventoAtributos!![5], cat!!,
                horaInicio, horaFin, eventoAtributos!![7])
        txtNombreEvento.setText("")
        txtNombreUsuario.setText("")
        val intent = Intent(this, EventoCreadorActivity::class.java)
        intent.putExtra("eventoSeleccionado", eventoAtributos)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun enviarEvento(titulo: String, nombre: String, hora: String, categoria: String,
                             horaInicio: String, horaFin: String, eMail: String) {
        eventoAtributos!![0]= titulo
        eventoAtributos!![1]= nombre
        eventoAtributos!![2]= categoria
        eventoAtributos!![3]= horaInicio
        eventoAtributos!![4]= horaFin

        val ev = Evento(titulo, nombre, hora, categoria, horaInicio, horaFin, eMail)
        mMessageReferencia!!.child(eventoAtributos!![6]).setValue(ev)
    }
}