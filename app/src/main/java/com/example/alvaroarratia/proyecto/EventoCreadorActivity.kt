package com.example.alvaroarratia.proyecto

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.evento_creador.*
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.Toast

class EventoCreadorActivity: AppCompatActivity(), View.OnClickListener {

    private var eventoAtributos: ArrayList<String>? = null

    private var mDatabase: DatabaseReference? = null
    private var mMessageReferencia: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.evento_creador)

        mDatabase = FirebaseDatabase.getInstance().reference
        mMessageReferencia = FirebaseDatabase.getInstance().getReference("eventos")

        eventoAtributos = ArrayList()
        eventoAtributos = intent.getStringArrayListExtra("eventoSeleccionado")

        txtTituloEvC.text = eventoAtributos!![0]
        txtNombreCreadorC.text = eventoAtributos!![1]
        txtCategoriaC.text = eventoAtributos!![2]
        txtHraInicioC.text = eventoAtributos!![3]
        txtHraFinC.text = eventoAtributos!![4]

        btn_participantes.setOnClickListener(this)
        btn_borrarEvento.setOnClickListener(this)
        btn_editarEvento.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val i = view!!.id
        when (i) {
            R.id.btn_participantes -> {
                val intent = Intent(this, UsuarioActivity::class.java)
                startActivity(intent)
                //TODO("Hacer la solicitud y cambiar la clase del intent")
            }
            R.id.btn_borrarEvento -> {
                borrarEvento(eventoAtributos!![6])
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            R.id.btn_editarEvento -> {
                val intent = Intent(this, EditarEventoActivity::class.java)
                intent.putExtra("eventoSeleccionado", eventoAtributos)
                startActivity(intent)
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

    fun borrarEvento(id: String) {
        mMessageReferencia!!.child(id).removeValue()
        //TODO("Agregar un alert")
    }

}
