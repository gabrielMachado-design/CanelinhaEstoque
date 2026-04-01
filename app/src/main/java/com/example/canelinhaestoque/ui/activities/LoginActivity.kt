package com.example.canelinhaestoque.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.canelinhaestoque.MainActivity
import com.example.canelinhaestoque.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {


    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkUserSession()
        setupLoginButton()
    }


    private fun checkUserSession() {
        if (auth.currentUser != null) {
            navigateToInventory()
        }
    }

    private fun setupLoginButton() {

        val emailInput = findViewById<TextInputEditText>(R.id.user)
        val passwordInput = findViewById<TextInputEditText>(R.id.password)
        val emailLayout = findViewById<TextInputLayout>(R.id.layoutUser)
        val passwordLayout = findViewById<TextInputLayout>(R.id.layoutPassword)
        val loginButton = findViewById<MaterialButton>(R.id.btnEntrar)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()


            emailLayout.error = null
            passwordLayout.error = null


            if (email.isEmpty()) {
                    emailLayout.error = "Digite seu E-mail"
                emailInput.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordLayout.error = "Digite sua senha"
                passwordInput.requestFocus()
                return@setOnClickListener
            }


            loginButton.isEnabled = false
            loginButton.text = "Autenticando"


            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show()
                    navigateToInventory()
                }
                .addOnFailureListener { exception ->
                    loginButton.isEnabled = true
                    loginButton.text = "Entrar"

                    val errorMessage = when (exception.message) {
                        null -> "Authentication failed"
                        else -> "Invalid credentials. Please try again."
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun navigateToInventory() {
        val intent = Intent(this, MainActivity::class.java).apply {

            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}