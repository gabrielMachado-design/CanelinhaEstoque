package com.example.canelinhaestoque.ui.screens

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

/**
 * Professional Login Screen for CanelinhaEstoque
 * Handles user authentication via Firebase Auth.
 */
class LoginActivity : AppCompatActivity() {

    // Firebase Authentication instance
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkUserSession()
        setupLoginButton()
    }

    /**
     * If user is already logged in, skip this screen.
     */
    private fun checkUserSession() {
        if (auth.currentUser != null) {
            navigateToInventory()
        }
    }

    private fun setupLoginButton() {
        // UI References
        val emailInput = findViewById<TextInputEditText>(R.id.user)
        val passwordInput = findViewById<TextInputEditText>(R.id.password)
        val emailLayout = findViewById<TextInputLayout>(R.id.layoutUser)
        val passwordLayout = findViewById<TextInputLayout>(R.id.layoutPassword)
        val loginButton = findViewById<MaterialButton>(R.id.btnEntrar)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Reset errors
            emailLayout.error = null
            passwordLayout.error = null

            // Validation Logic
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

            // Firebase Authentication Process
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

    /**
     * Closes login and starts the main inventory screen.
     */
    private fun navigateToInventory() {
        val intent = Intent(this, MainActivity::class.java).apply {
            // Clears activity stack so user can't go back to login
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}