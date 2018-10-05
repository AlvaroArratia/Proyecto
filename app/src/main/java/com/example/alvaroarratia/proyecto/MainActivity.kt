package com.example.alvaroarratia.proyecto

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.view.Menu
import android.view.MenuItem
import kotlin.collections.ArrayList
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity: AppCompatActivity(), View.OnClickListener {

    private var mDatabase: DatabaseReference? = null
    private var mMessageReferencia: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser? = null

    private var eventos: ArrayList<Evento>? = null
    private var keys: ArrayList<String>? = null
    private var adapter: EventoAdapter? = null

    private val TAG = "ChatActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        eventos = ArrayList()
        keys = ArrayList()

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        mDatabase = FirebaseDatabase.getInstance().reference
        mMessageReferencia = FirebaseDatabase.getInstance().getReference("eventos")
        mAuth = FirebaseAuth.getInstance()
        user = mAuth!!.currentUser

        escucharEvento()

        adapter = EventoAdapter(eventos!!)
        recyclerView.adapter = adapter

        adapter!!.setOnClickListener(this)
        btn_agregar.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu items for use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onClick(view: View?) {
        val i = view!!.id
        when (i) {
            R.id.btn_agregar -> {
                val intent = Intent(this, CrearActivity::class.java)
                intent.putExtra("userId", user!!.uid)
                startActivity(intent)
            }
            else -> {
                irAEvento(view)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
        when (item.itemId) {
            R.id.usuario -> {
                //val intent = Intent(this, UsuarioActivity::class.java)
                //startActivity(intent)
                return true
            }
            R.id.logOut -> {
                logout()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun irAEvento(view: View?) {
        val position = recyclerView.getChildAdapterPosition(view)
        Log.i("Posicion array", "" + position + "")
        val event = eventos!![position]
        val key = keys!![position]
        val eventoAtributos = arrayListOf(event.titulo, event.nombre, event.categoria,
                event.horaInicio, event.horaFin, event.hora, key, event.uid)
        if (event.uid.equals(user!!.uid)) {
            val intent = Intent(this, EventoCreadorActivity::class.java)
            intent.putExtra("eventoSeleccionado", eventoAtributos)
            startActivity(intent)
        } else if (event.uid != user!!.uid) {
            val intent = Intent(this, EventoParticipanteActivity::class.java)
            intent.putExtra("eventoSeleccionado", eventoAtributos)
            startActivity(intent)
        }
    }

    private fun escucharEvento() {
        eventos!!.clear()
        keys!!.clear()
        val escuchadorEventos = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val evData = data.getValue<Evento>(Evento::class.java)
                    val ev = evData?.let { it } ?: continue
                    eventos!!.add(ev)
                    keys!!.add(data.key!!)
                    Log.e(TAG, "onDataChange: Message data is updated: " + ev!!.toString())
                    Log.i("KEY", data.key)
                }
                eventos!!.reverse()
                keys!!.reverse()
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.i(TAG, "Error al escuchar eventos")
            }
        }
        mMessageReferencia!!.addValueEventListener(escuchadorEventos)
    }

    private fun goLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()
        goLoginScreen()
    }

}
