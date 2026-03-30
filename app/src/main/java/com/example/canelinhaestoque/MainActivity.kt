package com.example.canelinhaestoque

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.canelinhaestoque.ui.screens.AddProductActivity
import com.example.canelinhaestoque.ui.screens.HomeScreen
import com.example.canelinhaestoque.ui.screens.ProductListScreen
import com.example.canelinhaestoque.viewmodel.ProductViewModel
import kotlin.jvm.java


// 🔹 Controle de telas
enum class Screen {
    HOME,
    PRODUCTS
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ProductViewModel()

        setContent {


            var currentScreen by remember { mutableStateOf(Screen.HOME) }

            when (currentScreen) {


                Screen.HOME -> {
                    HomeScreen(
                        onEstoqueClick = {
                            currentScreen = Screen.PRODUCTS
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
                        }
                    )
                }
            }
        }
    }
}

