package com.example.firebasepractica7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

class GithubAuthActivity : AppCompatActivity() {
    private lateinit var inputEmail: EditText
    private lateinit var btnGitHub: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_auth)

        inputEmail = findViewById(R.id.inputEmail)
        auth = FirebaseAuth.getInstance()
        btnGitHub = findViewById(R.id.btnGithubLogin)
        btnGitHub.setOnClickListener{
            gitHubLogin()
        }
    }

    private fun gitHubLogin() {
        val email = inputEmail.getText().toString()
        val provider = OAuthProvider.newBuilder("github.com")
        // Target specific email with login hint.
        provider.addCustomParameter("login", email)
        provider.scopes = listOf("user:email")
        val pendingResultTask = auth.pendingAuthResult
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                .addOnSuccessListener {
                    // User is signed in.
                    // IdP data available in
                    // authResult.getAdditionalUserInfo().getProfile().
                    // The OAuth access token can also be retrieved:
                    // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                    // The OAuth secret can be retrieved by calling:
                    // ((OAuthCredential)authResult.getCredential()).getSecret().
                }
                .addOnFailureListener {exception->
                    // Handle failure.
                    Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
        } else {
            auth
                .startActivityForSignInWithProvider( /* activity = */this, provider.build())
                .addOnSuccessListener {
                   openNextActivity()
                }
                .addOnFailureListener {exception->
                    Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
                    // Handle failure.
                }
        }
    }

    private fun openNextActivity() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}