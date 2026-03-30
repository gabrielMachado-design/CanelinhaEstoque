package com.example.canelinhaestoque.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Print // Importação correta
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.canelinhaestoque.viewmodel.ProductViewModel

@Composable
fun ProductListScreen(
    viewModel: ProductViewModel,
    onAddClick: () -> Unit
) {
    val products by viewModel.products
    var search by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Carrega os produtos em tempo real
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    // Lógica de busca
    val displayedList = if (search.isEmpty()) {
        products.reversed()
    } else {
        products.filter { it.name.contains(search, ignoreCase = true) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Cabeçalho com Campo de Busca e Botão de Gerar PDF
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    label = { Text("Buscar produto") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // 📄 BOTÃO GERAR PDF (Ícone de Impressora)
                IconButton(
                    onClick = { viewModel.generatePdfReport(context) },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        // AQUI ESTAVA O ERRO: Mudamos para Icons.Filled.Print
                        imageVector = Icons.Filled.Print,
                        contentDescription = "Gerar PDF",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // 📦 LISTA DE PRODUTOS
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(displayedList) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
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
                                Text("Valor: R$ ${product.salePrice}")
                                Text("Quantidade: ${product.stockQuantity}")
                            }

                            // 🗑️ BOTÃO EXCLUIR
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

        // ➕ BOTÃO FLUTUANTE (FAB) - Fixo no canto inferior direito
        FloatingActionButton(
            onClick = onAddClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Text("+", style = MaterialTheme.typography.headlineSmall)
        }
    }
}