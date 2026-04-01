package com.example.canelinhaestoque.ui.screens

import android.R.color.white
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.canelinhaestoque.viewmodel.ProductViewModel
import androidx.compose.material.icons.filled.Search

val canelinhaRed = Color(0xFFCE1717)
val darkRed = Color(0xFFB71C1C)
val lightGrayText = Color(0xFF9E9E9E)
@Composable
fun ProductListScreen(
    viewModel: ProductViewModel,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit
) {

    val products by viewModel.products
    var search by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }


    val displayedList = if (search.isEmpty()) {
        products.takeLast(20).reversed()
    } else {
        products.filter {
            it.name.contains(search, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F8F8))) {

        // 🔝 TOP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔙 VOLTAR
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Voltar",
                    tint = Color(0xFFCE1717))
            }

            Spacer(modifier = Modifier.weight(1f))

            // ➕ ADICIONAR
            IconButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar",
                    tint = canelinhaRed)
            }
        }


        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp),            shadowElevation = 6.dp,
            color = Color.White
        ) {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Buscar produto...", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = canelinhaRed,
                    unfocusedBorderColor = Color.Transparent,
                    focusedLabelColor = canelinhaRed,
                    cursorColor = canelinhaRed
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = canelinhaRed
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 📦 LISTA
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            items(displayedList) { product ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(canelinhaRed)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,

                    ) {

                        Column(modifier = Modifier.weight(1f)) {

                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFFFFFFFF),
                                fontWeight = FontWeight.Bold
                            )

                            Text("Quantidade: ${product.stockQuantity}",
                                color = Color(0xFFFFFFFF),
                                style = MaterialTheme.typography.bodySmall)

                            Text("Custo: R$ ${product.costPrice}",
                                color = Color(0xFFFFFFFF),
                                style = MaterialTheme.typography.bodySmall)

                            Text("Venda: R$ ${product.salePrice}",
                                color = Color(0xFFFFFFFF),
                                style = MaterialTheme.typography.bodySmall)
                        }

                        // 🗑 DELETE
                        IconButton(onClick = {
                            if (product.id.isNotEmpty()) {
                                viewModel.deleteProduct(product.id)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Excluir",
                                tint = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}