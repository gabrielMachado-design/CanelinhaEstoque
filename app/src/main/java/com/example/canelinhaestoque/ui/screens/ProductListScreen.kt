package com.example.canelinhaestoque.ui.screens

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
import androidx.compose.ui.unit.dp
import com.example.canelinhaestoque.viewmodel.ProductViewModel

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

    // 🔥 Últimos 20 ou busca
    val displayedList = if (search.isEmpty()) {
        products.takeLast(20).reversed()
    } else {
        products.filter {
            it.name.contains(search, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // 🔝 TOP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔙 VOLTAR
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
            }

            Spacer(modifier = Modifier.weight(1f))

            // ➕ ADICIONAR
            IconButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
            }
        }

        // 🔍 BUSCA
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("Buscar produto") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

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
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(modifier = Modifier.weight(1f)) {

                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("Quantidade: ${product.stockQuantity}")

                            Text("Custo: R$ ${product.costPrice}")

                            Text("Venda: R$ ${product.salePrice}")
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
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}