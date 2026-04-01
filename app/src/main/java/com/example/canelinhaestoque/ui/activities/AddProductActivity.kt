package com.example.canelinhaestoque.ui.activities

import android.os.Bundle
import android.widget.ImageButton
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

        setContentView(R.layout.activity_add_product)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etCostPrice = findViewById<TextInputEditText>(R.id.etCostPrice)
        val etPrice = findViewById<TextInputEditText>(R.id.etPrice)
        val etStock = findViewById<TextInputEditText>(R.id.etStock)
        val btnSave = findViewById<MaterialButton>(R.id.btnSave)

        val repo = ProductRepository()

        btnBack.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val costPriceText = etCostPrice.text.toString().trim()
            val priceText = etPrice.text.toString().trim()
            val stockText = etStock.text.toString().trim()


            if (name.isEmpty()) {
                etName.error = "Digite o nome do produto"
                return@setOnClickListener
            }

            if (priceText.isEmpty()) {
                etPrice.error = "Digite o preço de venda"
                return@setOnClickListener
            }


            val costPrice = costPriceText.toDoubleOrNull() ?: 0.0
            val price = priceText.toDoubleOrNull() ?: 0.0
            val stock = stockText.toDoubleOrNull() ?: 0.0


            val product = Product(
                id = "",
                name = name,
                description = "",
                costPrice = costPrice,
                salePrice = price,
                stockQuantity = stock
            )


            repo.addProduct(
                product = product,
                onSuccess = {
                    Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                },
                onFailure = { exception ->
                    Toast.makeText(this, "Erro ao salvar: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}