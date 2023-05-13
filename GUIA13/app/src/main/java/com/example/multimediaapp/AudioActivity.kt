package com.example.multimediaapp

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import java.io.IOException

class AudioActivity : AppCompatActivity() {

    private lateinit var Media: MediaPlayer
    private lateinit var Play: Button
    private lateinit var Stop: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        Play = findViewById(R.id.play)
        Stop = findViewById(R.id.stop)

        Play.setOnClickListener(onClick)
        Stop.setOnClickListener(onClick)

        Media = MediaPlayer.create(applicationContext,R.raw.music)

    }

    private val onClick = View.OnClickListener {
        when(it.id){
            R.id.play -> {
                if(Media.isPlaying){
                    Media.pause()
                    Play.text = "Play"
                }else{
                    Media.start()
                    Play.text = "Pause"
                }
            }
            R.id.stop->{
                Media.stop()
                Play.text = "Play"
                try {
                    Media.prepare()
                }catch (e: IllegalStateException){
                    e.printStackTrace()
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }
        }
    }
}