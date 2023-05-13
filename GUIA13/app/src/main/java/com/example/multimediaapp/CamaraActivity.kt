package com.example.multimediaapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class CamaraActivity : AppCompatActivity() {

    private var TAG: String = "INFORMACION"
    private lateinit var TomarFoto: Button
    private lateinit var btnPermiso: Button
    private lateinit var textMensaje: TextView
    private val CAMERA_REQUEST_CODE = 0
    private val STORAGE_REQUEST_CODE = 0
    private val REQUEST_IMAGE_CAPTURE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camara)
        TomarFoto = findViewById(R.id.btnTomarFoto)
        btnPermiso = findViewById(R.id.btnPermisoCamara)
        textMensaje = findViewById(R.id.txtMensajeCamara)
        TomarFoto!!.isEnabled = false

        textMensaje.text = getString(R.string.mensajepermisos)
        TomarFoto.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 0)
        }
        btnPermiso.setOnClickListener {
            this.checkPermisoCamara()
        }
    }
    private fun checkPermisoCamara() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "Solicitud de permiso")
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                Log.d(TAG, "Debe de dirigirse a ajustes del telefono")
                textMensaje.text = "Debe de dirigirse a ajustes del telefono"
            } else {
                //El usuario nunca ha aceptado ni rechazado,
                // así que le pedimos que acepte el permiso.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE
                )
            }
        } else {
            Log.d(TAG, "El permiso de la camara ha sido concedido")
            TomarFoto!!.isEnabled = true
            textMensaje.text = "El permiso de la camara ha sido concedido"
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val imageView = findViewById<ImageView>(R.id.imgFoto)
            imageView.setImageBitmap(imageBitmap)
        }
    }
}