package com.example.appcalculadoramvc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var controller: CalculatorController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controller = CalculatorController(this)
    }
    fun showToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
    fun showSnackbar(message: String){
        Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_SHORT).show()
    }
}