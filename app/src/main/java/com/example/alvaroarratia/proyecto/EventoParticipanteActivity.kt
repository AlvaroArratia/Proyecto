package com.example.alvaroarratia.proyecto

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.evento_creador.*
import kotlinx.android.synthetic.main.evento_participante.*

class EventoParticipanteActivity : AppCompatActivity() , View.OnClickListener {

    private var eventoAtributos: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.evento_participante)

        eventoAtributos = ArrayList()
        eventoAtributos = intent.getStringArrayListExtra("eventoSeleccionado")

        txtTituloEv.text = eventoAtributos!![0]
        txtNombreCreador.text = eventoAtributos!![1]
        txtCategoria.text = eventoAtributos!![2]
        txtHraInicio.text = eventoAtributos!![3]
        txtHraFin.text = eventoAtributos!![4]

        btn_participar.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val i = view!!.id
        when (i) {
            R.id.btn_participar -> {
                //manda la solicitud
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

}
