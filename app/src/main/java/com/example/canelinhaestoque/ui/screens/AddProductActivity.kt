package com.example.canelinhaestoque.ui.screens

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.canelinhaestoque.R
import com.example.canelinhaestoque.data.model.Product
import com.example.canelinhaestoque.data.repository.ProductRepository

class AddProductActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Define o layout XML (certifique-se que o arquivo activity_add_product.xml existe)
        setContentView(R.layout.activity_add_product)

        // 2. Referência aos componentes do XML
        val etName = findViewById<EditText>(R.id.etName)
        val etPrice = findViewById<EditText>(R.id.etPrice)
        val etStock = findViewById<EditText>(R.id.etStock)
        val btnSave = findViewById<Button>(R.id.btnSave)

        val repo = ProductRepository()

        // 3. Lógica do botão salvar
        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val priceText = etPrice.text.toString().trim()
            val stockText = etStock.text.toString().trim()

            // Validação básica
            if (name.isEmpty()) {
                etName.error = "Digite o nome"
                return@setOnClickListener
            }

            val price = priceText.toDoubleOrNull() ?: 0.0
            val stock = stockText.toIntOrNull() ?: 0

            // Criando o objeto Produto (certifique-se que seu model Product aceita esses campos)
            val product = Product(
                id = "", // O Firestore gera o ID automaticamente se configurado no repo
                name = etName.text.toString(),
                salePrice = etPrice.text.toString().toDouble(),
                stockQuantity = etStock.text.toString().toInt()
            )

            // Chamada ao repositório
            repo.addProduct(
                product = product,
                onSuccess = {
                    Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    finish() // Fecha a tela e volta para a lista
                },
                onFailure = { exception ->
                    Toast.makeText(this, "Erro ao salvar: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}