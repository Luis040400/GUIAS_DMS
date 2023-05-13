package com.example.multimediaapp

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class VideoActivity : AppCompatActivity() {

    private lateinit var mediacontrol: MediaController
    private lateinit var textVideo: TextView
    private lateinit var video: VideoView
    private var TAG: String = "INFORMACION"
    private lateinit var btnVideo: Button
    private lateinit var btnPermiso: Button
    private val STORAGE_REQUEST_CODE = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        btnVideo = findViewById(R.id.idCargarVideo)
        btnPermiso = findViewById(R.id.idSolicitarPermisos)
        textVideo = findViewById(R.id.textVideo)
        btnVideo!!.isEnabled = false
        btnVideo.setOnClickListener {
            textVideo.text = getString(R.string.mensajevideo)
            video = findViewById(R.id.video)
            //Accediendo al archivo almacenado en nuestro dispositivo
            var f = File(
                Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/Download/",
                "video.3gp"
            )
            if (f.exists()) {
                val uri: Uri = Uri.fromFile(f)
                video.setVideoURI(uri)
                mediacontrol = MediaController(this)
                video.setMediaController(mediacontrol)
                mediacontrol.show()
            }
        }
        btnPermiso.setOnClickListener{
            this.checkPermisoStorage()
        }
    }
    private fun checkPermisoStorage() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Informacion", "Solicitud de permiso storage")
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                Log.d("Informacion", "Debe de dirigirse a ajustes del telefono")
                textVideo.text = "Debe de dirigirse a ajustes del telefono"
            } else {
                //El usuario nunca ha aceptado ni rechazado,
                // así que le pedimos que acepte el permiso.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_REQUEST_CODE
                )
            }
        } else {
            Log.d("Informacion", "Se ha concedido permiso al almacenamiento")
            textVideo.text = "Se ha concedido permiso al almacenamiento"
            btnVideo!!.isEnabled = true
        }
    }

}