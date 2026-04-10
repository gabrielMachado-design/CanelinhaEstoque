package com.example.canelinhaestoque.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canelinhaestoque.ui.activities.EditProductActivity
import com.example.canelinhaestoque.viewmodel.ProductViewModel


val canelinhaRed = Color(0xFFCE1717)
val darkRed = Color(0xFFB71C1C)
val lightGrayText = Color(0xFFD1D1D1)

@Composable
fun ProductListScreen(
    viewModel: ProductViewModel,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
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

        // TOP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // VOLTAR
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = canelinhaRed
                )
            }

            // Título
            Text(
                text = "Canelinha Produtos",
                color = canelinhaRed,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // ADICIONAR
            IconButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar",
                    tint = canelinhaRed
                )
            }
        }

        // BUSCA COM SOMBRA E BORDAS REDONDAS
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(28.dp),
            shadowElevation = 6.dp,
            color = Color.White
        ) {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Buscar produto...", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = canelinhaRed,
                    unfocusedBorderColor = Color.Transparent,
                    focusedLabelColor = canelinhaRed,
                    cursorColor = canelinhaRed
                ),
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = canelinhaRed)
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 📦 LISTA DE PRODUTOS
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(displayedList) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable {
                            // LOGICA  ABRIR TELA  EDIÇÃO
                            val intent = Intent(context, EditProductActivity::class.java).apply {
                                putExtra("PRODUCT_ID", product.id)
                                putExtra("PRODUCT_NAME", product.name)
                                putExtra("PRODUCT_COST", product.costPrice)
                                putExtra("PRODUCT_PRICE", product.salePrice)
                                putExtra("PRODUCT_STOCK", product.stockQuantity)
                            }
                            context.startActivity(intent)
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(canelinhaRed) // Célula toda em Vermelho
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            // Nome em Branco
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))


                            Text(
                                text = "Quantidade: ${product.stockQuantity}",
                                color = lightGrayText,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Custo: R$ ${product.costPrice}",
                                color = lightGrayText,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Venda: R$ ${product.salePrice}",
                                color = lightGrayText,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        //  DELETE
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