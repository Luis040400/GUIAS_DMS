package edu.udb.realtimedatabase

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import edu.udb.realtimedatabase.PersonasActivity.Companion.database
import edu.udb.realtimedatabase.datos.Persona
import java.text.SimpleDateFormat
import java.util.*

class AddPersonaActivity : AppCompatActivity() {
    private var edtDUI: EditText? = null
    private var edtNombre: EditText? = null
    private var edtFecha: EditText? = null
    private var spGenero: Spinner? = null
    private var edtPeso: EditText? = null
    private var edtAltura: EditText? = null
    private var key = ""
    private var nombre = ""
    private var dui = ""
    private var fecha = ""
    private var genero = ""
    private var peso = ""
    private var altura = ""
    private var accion = ""
    private lateinit var  database:DatabaseReference

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_persona)
        inicializar()
    }

    private fun inicializar() {
        edtNombre = findViewById<EditText>(R.id.edtNombre)
        edtDUI = findViewById<EditText>(R.id.edtDUI)
        edtFecha = findViewById<EditText>(R.id.edtFecha)
        spGenero = findViewById<Spinner>(R.id.spGenero)
        edtPeso = findViewById<EditText>(R.id.edtPeso)
        edtAltura = findViewById<EditText>(R.id.edtAltura)
        val edtNombre = findViewById<EditText>(R.id.edtNombre)
        val edtDUI = findViewById<EditText>(R.id.edtDUI)
        val edtFecha = findViewById<EditText>(R.id.edtFecha)
        val spGenero = findViewById<Spinner>(R.id.spGenero)
        val edtPeso = findViewById<EditText>(R.id.edtPeso)
        val edtAltura = findViewById<EditText>(R.id.edtAltura)

        // Obtención de datos que envia actividad anterior
        val datos: Bundle? = intent.getExtras()
        if (datos != null) {
            key = datos.getString("key").toString()
        }
        if (datos != null) {
            edtDUI.setText(intent.getStringExtra("dui").toString())
        }
        if (datos != null) {
            edtNombre.setText(intent.getStringExtra("nombre").toString())
        }
        if (datos != null) {
            edtFecha.setText(intent.getStringExtra("birthday").toString())
        }
        if (datos != null) {
            val valores = resources.getStringArray(R.array.genero)
            val text = intent.getStringExtra("genero").toString()
            val indice = valores.indexOf(text)
            spGenero.setSelection(indice)
        }
        if (datos != null) {
            edtPeso.setText(intent.getStringExtra("peso").toString())
        }
        if (datos != null) {
            edtAltura.setText(intent.getStringExtra("altura").toString())
        }
        if (datos != null) {
            accion = datos.getString("accion").toString()
        }

    }


    fun guardar(v: View?) {
        val nombre = edtNombre?.text.toString()
        val dui = edtDUI?.text.toString()
        val birthday = edtFecha?.text.toString()
        val genero = spGenero?.getSelectedItem().toString()
        val peso = edtPeso?.text.toString()
        val altura = edtAltura?.text.toString()
        database=FirebaseDatabase.getInstance().getReference("personas")
        // Se forma objeto persona
        val persona = Persona(dui, nombre,birthday,genero,peso,altura)

        Log.d("PERSONA_OBJ",nombre)
        Log.d("PERSONA_OBJ",dui)
        Log.d("PERSONA_OBJ",birthday)
        Log.d("PERSONA_OBJ",genero)
        Log.d("PERSONA_OBJ",peso)
        Log.d("PERSONA_OBJ",altura)

        if (accion == "a") { //Agregar registro
            database.child(nombre).setValue(persona).addOnSuccessListener {
                Toast.makeText(this,"Se guardo con exito", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this,"Failed ", Toast.LENGTH_SHORT).show()
            }
        } else  // Editar registro
        {
            val key = database.child("nombre").push().key
            if (key == null) {
                Toast.makeText(this,"Llave vacia", Toast.LENGTH_SHORT).show()
            }
            val personasValues = persona.toMap()
            val childUpdates = hashMapOf<String, Any>(
                "$nombre" to personasValues
            )
            database.updateChildren(childUpdates)
            Toast.makeText(this,"Se actualizo con exito", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    fun cancelar(v: View?) {
        finish()
    }
}