package com.example.firebasepractica7

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class RegisterActivity : AppCompatActivity() {

    //creamos la referencia del objeto FirebaseAuth
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    //referencia a componentes de nuestro Layout
    private lateinit var buttonRegister: Button
    private lateinit var textViewLogin:TextView
    private lateinit var btnGoogle: Button
    private lateinit var btnGitHub: Button

    //escuchador de FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Inicializamos el objeto FirebaseAuth
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        btnGoogle = findViewById<Button?>(R.id.googleButton)
        btnGoogle.setOnClickListener{
            signInGoogle()
        }

        btnGitHub = findViewById(R.id.githubButton);
        btnGitHub.setOnClickListener{
            val intent = Intent(this,GithubAuthActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonRegister = findViewById(R.id.btnRegister)
        buttonRegister.setOnClickListener{
            val email = findViewById<EditText>(R.id.txtEmail).text.toString()
            val password = findViewById<EditText>(R.id.txtPass).text.toString()
            this.register(email,password)
        }

        textViewLogin = findViewById(R.id.textViewLogin)
        textViewLogin.setOnClickListener { this.goToLogin() }

        //validar si existe un usuario activo
        this.checkUser()
        btnGoogle = findViewById(R.id.googleButton)
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handlerResults(task)
        }
    }

    private fun handlerResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if(account != null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this,task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onPause() {
        super.onPause()
        auth.removeAuthStateListener(authStateListener)
    }
    private fun register(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{task ->
            if(task.isSuccessful){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
    private fun checkUser(){
        //Verificación del usuario
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            if(auth.currentUser != null){
                //cambiando la vista
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    private fun goToLogin(){
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }
}