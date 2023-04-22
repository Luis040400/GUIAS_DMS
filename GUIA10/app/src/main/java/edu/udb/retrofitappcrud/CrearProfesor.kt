package edu.udb.retrofitappcrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CrearProfesor : AppCompatActivity() {


    private lateinit var nombreEditText: EditText
    private lateinit var apellidoEditText: EditText
    private lateinit var edadEditText: EditText
    private lateinit var crearButton: Button

    var auth_username = ""
    var auth_password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_profesor)

        val datos: Bundle? = intent.extras
        if(datos != null){
            auth_username = datos.getString("auth_username").toString()
            auth_password = datos.getString("auth_password").toString()
        }

        nombreEditText = findViewById(R.id.editTextNombreProfesor)
        apellidoEditText = findViewById(R.id.editTextApellidoProfesor)
        edadEditText = findViewById(R.id.editTextEdadProfesor)
        crearButton = findViewById(R.id.btnGuardarProfesor)

        crearButton.setOnClickListener{
            val nombre = nombreEditText.text.toString()
            val apellido = apellidoEditText.text.toString()
            val edad = edadEditText.text.toString().toInt()
            val profesor = Profesor(0,nombre,apellido,edad)

            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", Credentials.basic(auth_username, auth_password))
                        .build()
                    chain.proceed(request)
                }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.56.1:80/GUIADSM/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            val api = retrofit.create(ProfesorApi::class.java)

            api.crearProfesor(profesor).enqueue(object : Callback<Profesor> {
                override fun onResponse(call: Call<Profesor>, response: Response<Profesor>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CrearProfesor, "Profesor creado exitosamente", Toast.LENGTH_SHORT).show()
                        val i = Intent(getBaseContext(), ProfesorMain::class.java)
                        startActivity(i)
                    } else {
                        val error = response.errorBody()?.string()
                        Log.e("API", "Error crear alumno: $error")
                        Toast.makeText(this@CrearProfesor, "Error al crear el profesor", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<Profesor>, t: Throwable) {
                    Toast.makeText(this@CrearProfesor, "Error al crear el profesor", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}