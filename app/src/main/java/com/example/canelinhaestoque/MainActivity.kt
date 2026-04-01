package com.example.canelinhaestoque

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.canelinhaestoque.ui.activities.AddProductActivity
import com.example.canelinhaestoque.ui.screens.HomeScreen
import com.example.canelinhaestoque.ui.activities.LoginActivity
import com.example.canelinhaestoque.ui.screens.ProductListScreen
import com.example.canelinhaestoque.viewmodel.ProductViewModel
import com.google.firebase.auth.FirebaseAuth

// 🔹 Controle de telas
enum class Screen {
    HOME,
    PRODUCTS
}

class MainActivity : ComponentActivity() {

    // 🔥 ViewModel fora (correto)
    private val viewModel = ProductViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔐 LOGIN (mantido)
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContent {

            var currentScreen by remember { mutableStateOf(Screen.HOME) }

            when (currentScreen) {

                // 🏠 HOME
                Screen.HOME -> {
                    HomeScreen(
                        onEstoqueClick = {
                            currentScreen = Screen.PRODUCTS
                        }
                    )
                }

                // 📦 PRODUTOS
                Screen.PRODUCTS -> {
                    ProductListScreen(
                        viewModel = viewModel,

                        onAddClick = {
                            startActivity(
                                Intent(this@MainActivity, AddProductActivity::class.java)
                            )
                        },

                        // 🔥 BOTÃO VOLTAR FUNCIONANDO
                        onBackClick = {
                            currentScreen = Screen.HOME
                        }
                    )
                }
            }
        }
    }

    // 🔥 Atualiza lista ao voltar do cadastro
    override fun onResume() {
        super.onResume()
        viewModel.loadProducts()
    }
}
