package com.example.canelinhaestoque.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canelinhaestoque.viewmodel.ProductViewModel
import com.example.canelinhaestoque.viewmodel.SaleViewModel

@Composable
fun SaleScreen(
    saleViewModel: SaleViewModel,
    productViewModel: ProductViewModel,
    onBackClick: () -> Unit
) {
    val saleItems = saleViewModel.items
    val products by productViewModel.products

    var discountInput by remember { mutableStateOf("0.0") }
    var showProductDialog by remember { mutableStateOf(false) }
    var itemToEditQuantity by remember { mutableStateOf<com.example.canelinhaestoque.data.model.SaleItem?>(null) }
    var quantityInput by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        productViewModel.loadProducts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .statusBarsPadding() // Evita que o topo fique sob a barra de status
    ) {
        // TOP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = canelinhaRed)
            }
            Text(
                text = "Nova Venda",
                color = canelinhaRed,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { showProductDialog = true }) {
                Icon(Icons.Default.AddShoppingCart, contentDescription = "Add Item", tint = canelinhaRed)
            }
        }

        // LISTA DE ITENS NA VENDA
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(saleItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable {
                            itemToEditQuantity = item
                            quantityInput = item.quantity.toString()
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(canelinhaRed)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(
                                text = "Qtd: ${item.quantity} x R$ ${item.unitPrice}",
                                color = lightGrayText,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Text(
                            text = "R$ ${String.format("%.2f", item.quantity * item.unitPrice)}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        IconButton(onClick = { saleViewModel.removeItem(item.productId) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Remover", tint = Color.Black)
                        }
                    }
                }
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding(),
            shadowElevation = 12.dp,
            color = Color.White,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Geral:", fontSize = 16.sp, color = Color.Gray)
                    Text(
                        "R$ ${String.format("%.2f", saleViewModel.getTotal())}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = canelinhaRed
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = canelinhaRed),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("FINALIZAR VENDA", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // DIALOG SELECIONAR PRODUTO
    if (showProductDialog) {
        AlertDialog(
            onDismissRequest = { showProductDialog = false },
            title = { Text("Escolha o Produto", color = canelinhaRed, fontWeight = FontWeight.Bold) },
            text = {
                LazyColumn(modifier = Modifier.height(300.dp)) {
                    items(products) { prod ->
                        ListItem(
                            modifier = Modifier.clickable {
                                saleViewModel.addProduct(prod)
                                showProductDialog = false
                            },
                            headlineContent = { Text(prod.name) },
                            supportingContent = {
                                Text("Estoque: ${prod.stockQuantity} | R$ ${prod.salePrice}")
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    tint = canelinhaRed
                                )
                            }
                        )
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showProductDialog = false }) { Text("Cancelar") }
            }
        )
    }

    // DIALOG AJUSTAR QUANTIDADE E DESCONTO
    if (itemToEditQuantity != null) {
        AlertDialog(
            onDismissRequest = {
                itemToEditQuantity = null
                discountInput = "0.0"
            },
            title = { Text("Ajustar Item", color = canelinhaRed, fontWeight = FontWeight.Bold) },
            text = {
                // CORREÇÃO: Envolvendo os campos em uma Column
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = quantityInput,
                        onValueChange = { quantityInput = it.replace(',', '.') },
                        label = { Text("Quantidade (ex: 1.5)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = canelinhaRed)
                    )
                    OutlinedTextField(
                        value = discountInput,
                        onValueChange = { discountInput = it.replace(',', '.') },
                        label = { Text("Desconto no Item (R$)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = canelinhaRed),
                        leadingIcon = { Text("R$", color = canelinhaRed, fontWeight = FontWeight.Bold) }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val q = quantityInput.toDoubleOrNull() ?: 1.0
                        saleViewModel.updatedQuantity(itemToEditQuantity!!.productId, q)
                        itemToEditQuantity = null
                        discountInput = "0.0"
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = canelinhaRed)
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    itemToEditQuantity = null
                    discountInput = "0.0"
                }) {
                    Text("Voltar", color = Color.Gray)
                }
            }
        )
    }
}