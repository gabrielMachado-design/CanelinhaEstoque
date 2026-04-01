package com.example.canelinhaestoque.ui.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.canelinhaestoque.R
import com.example.canelinhaestoque.data.model.Product
import com.example.canelinhaestoque.data.repository.ProductRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddProductActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Define o layout XML que criamos com bordas arredondadas e sombras
        setContentView(R.layout.activity_add_product)

        // Referenciando os novos IDs do Material Design
        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etCostPrice = findViewById<TextInputEditText>(R.id.etCostPrice)
        val etPrice = findViewById<TextInputEditText>(R.id.etPrice)
        val etStock = findViewById<TextInputEditText>(R.id.etStock)
        val btnSave = findViewById<MaterialButton>(R.id.btnSave)

        val repo = ProductRepository()

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val costPriceText = etCostPrice.text.toString().trim()
            val priceText = etPrice.text.toString().trim()
            val stockText = etStock.text.toString().trim()

            // Validação simples
            if (name.isEmpty()) {
                etName.error = "Digite o nome do produto"
                return@setOnClickListener
            }

            if (priceText.isEmpty()) {
                etPrice.error = "Digite o preço de venda"
                return@setOnClickListener
            }

            // Conversão segura: se o campo estiver vazio ou inválido, vira 0.0 ou 0
            val costPrice = costPriceText.toDoubleOrNull() ?: 0.0
            val price = priceText.toDoubleOrNull() ?: 0.0
            val stock = stockText.toIntOrNull() ?: 0

            // Criando o objeto produto com os nomes de campos corretos
            val product = Product(
                id = "", // O Firebase gerará o ID automaticamente
                name = name,
                description = "", // Campo removido do XML para simplificar
                costPrice = costPrice,
                salePrice = price,
                stockQuantity = stock
            )

            // Salvando no Firebase através do Repositório
            repo.addProduct(
                product = product,
                onSuccess = {
                    Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    finish() // Fecha a tela e volta para a listagem
                },
                onFailure = { exception ->
                    Toast.makeText(this, "Erro ao salvar: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}