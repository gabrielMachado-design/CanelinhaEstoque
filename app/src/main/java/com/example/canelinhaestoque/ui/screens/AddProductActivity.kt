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

        setContentView(R.layout.activity_add_product)

        val etCostPrice = findViewById<EditText>(R.id.etCostPrice)
        val etName = findViewById<EditText>(R.id.etName)
        val etPrice = findViewById<EditText>(R.id.etPrice)
        val etStock = findViewById<EditText>(R.id.etStock)
        val btnSave = findViewById<Button>(R.id.btnSave)


        val repo = ProductRepository()


        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val priceText = etPrice.text.toString().trim()
            val stockText = etStock.text.toString().trim()
            val costPriceText = etCostPrice.text.toString().trim()



            if (name.isEmpty()) {
                etName.error = "Digite o nome"
                return@setOnClickListener
            }
            val costPrice = costPriceText.toDoubleOrNull() ?: 0.00
            val price = priceText.toDoubleOrNull() ?: 0.00
            val stock = stockText.toIntOrNull() ?: 0


            val product = Product(
                id = "",
                name = etName.text.toString(),
                salePrice = etPrice.text.toString().toDouble(),
                stockQuantity = etStock.text.toString().toInt(),
                costPrice = etCostPrice.text.toString().toDouble()
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