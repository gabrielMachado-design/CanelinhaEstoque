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
import com.example.canelinhaestoque.ui.screens.SaleScreen
import com.example.canelinhaestoque.viewmodel.ProductViewModel
import com.example.canelinhaestoque.viewmodel.SaleViewModel
import com.google.firebase.auth.FirebaseAuth

enum class Screen {
    HOME,
    PRODUCTS,
    SALE
}

class MainActivity : ComponentActivity() {


    private val viewModel = ProductViewModel()
    private val saleViewModel = SaleViewModel()

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

                Screen.HOME -> {
                    HomeScreen(
                        onEstoqueClick = {
                            currentScreen = Screen.PRODUCTS
                        },
                        onSalesClick = {
                            currentScreen = Screen.SALE
                        }
                    )
                }


                Screen.PRODUCTS -> {
                    ProductListScreen(
                        viewModel = viewModel,

                        onAddClick = {
                            startActivity(
                                Intent(this@MainActivity, AddProductActivity::class.java)
                            )
                        },


                        onBackClick = {
                            currentScreen = Screen.HOME
                        }
                    )
                }
                Screen.SALE -> {
                    SaleScreen(
                        saleViewModel = saleViewModel,
                        productViewModel = viewModel,
                        onBackClick = {
                            currentScreen = Screen.HOME
                        }
                    )
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadProducts()
    }
}
