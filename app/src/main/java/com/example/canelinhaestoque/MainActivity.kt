package com.example.canelinhaestoque

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.canelinhaestoque.ui.activities.AddProductActivity
import com.example.canelinhaestoque.ui.activities.LoginActivity
import com.example.canelinhaestoque.ui.activities.SaleActivity // Sua nova Activity XML
import com.example.canelinhaestoque.ui.screens.HomeScreen
import com.example.canelinhaestoque.ui.screens.ProductListScreen
import com.example.canelinhaestoque.viewmodel.ProductViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.canelinhaestoque.ui.activities.ReportsActivity
import kotlin.jvm.java

enum class Screen {
    HOME,
    PRODUCTS
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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

                            startActivity(Intent(this@MainActivity, SaleActivity::class.java))
                        },
                        onReportsClick = {
                            startActivity(Intent(this@MainActivity, ReportsActivity::class.java))
                        }
                    )
                }

                Screen.PRODUCTS -> {
                    ProductListScreen(
                        viewModel = productViewModel,
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
            }
        }
    }

    override fun onResume() {
        super.onResume()

        productViewModel.loadProducts()
    }
}