package edu.udb.retrofitappcrud

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfesorMain : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProfesorAdapter
    private lateinit var api: ProfesorApi

    val auth_username = "admin"
    val auth_password = "admin123"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profesor_main)

        val fab_agregar: FloatingActionButton = findViewById<FloatingActionButton>(R.id.fab_agregar_profesor)

        recyclerView = findViewById(R.id.recyclerView_profesor)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val client = OkHttpClient.Builder()
            .addInterceptor{ chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization",Credentials.basic(auth_username,auth_password))
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.56.1:80/GUIADSM/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        api = retrofit.create(ProfesorApi::class.java)

        cargarDatos(api)

        fab_agregar.setOnClickListener(View.OnClickListener {
            val i = Intent(baseContext,CrearProfesor::class.java)
            i.putExtra("auth_username",auth_username)
            i.putExtra("auth_password",auth_password)
            startActivity(i)
        })
    }

    override fun onResume() {
        super.onResume()
        cargarDatos(api)
    }

    private fun cargarDatos(api: ProfesorApi) {
        val call = api.obtenerProfesor()
        call.enqueue(object : Callback<List<Profesor>> {
            override fun onResponse(call: Call<List<Profesor>>, response: Response<List<Profesor>>) {
                if (response.isSuccessful) {
                    val profesores = response.body()
                    if (profesores != null) {
                        adapter = ProfesorAdapter(profesores)
                        recyclerView.adapter = adapter

                        // Establecemos el escuchador de clics en el adaptador
                        adapter.setOnItemClickListener(object : ProfesorAdapter.OnItemClickListener {
                            override fun onItemClick(profesor: Profesor) {
                                val opciones = arrayOf("Modificar Alumno", "Eliminar Alumno")

                                AlertDialog.Builder(this@ProfesorMain)
                                    .setTitle(profesor.nombre)
                                    .setItems(opciones) { dialog, index ->
                                        when (index) {
                                            0 -> Modificar(profesor)
                                            1 -> eliminarProfesor(profesor, api)
                                        }
                                    }
                                    .setNegativeButton("Cancelar", null)
                                    .show()
                            }
                        })
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "Error al obtener los profesores: $error")
                    Toast.makeText(
                        this@ProfesorMain,
                        "Error al obtener los profesores 1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Profesor>>, t: Throwable) {
                Log.e("API", "Error al obtener los profesores: ${t.message}")
                Toast.makeText(
                    this@ProfesorMain,
                    "Error al obtener los profesores 2",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    private fun Modificar(profesor: Profesor) {
        val i = Intent(getBaseContext(), ActualizarProfesor::class.java)
        i.putExtra("profesor_id", profesor.id)
        i.putExtra("nombre", profesor.nombre)
        i.putExtra("apellido", profesor.apellido)
        i.putExtra("edad", profesor.edad)
        startActivity(i)
    }
    private fun eliminarProfesor(profesor: Profesor, api: ProfesorApi) {
        val profesorTMP = Profesor(profesor.id,"", "", -987)
        Log.e("API", "id : $profesor")
        val llamada = api.eliminarProfesor(profesor.id, profesorTMP)
        llamada.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfesorMain, "Profesor eliminado", Toast.LENGTH_SHORT).show()
                    cargarDatos(api)
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "Error al eliminar profesor : $error")
                    Toast.makeText(this@ProfesorMain, "Error al eliminar profesor 1", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API", "Error al eliminar profesor : $t")
                Toast.makeText(this@ProfesorMain, "Error al eliminar profesor 2", Toast.LENGTH_SHORT).show()
            }
        })
    }
}