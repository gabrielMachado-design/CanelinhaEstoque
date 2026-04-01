package com.example.canelinhaestoque.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.canelinhaestoque.R
import com.example.canelinhaestoque.data.model.Product
import com.example.canelinhaestoque.data.repository.ProductRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class EditProductActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        val btnBack = findViewById<android.widget.ImageButton>(R.id.btnBack)
        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etCostPrice = findViewById<TextInputEditText>(R.id.etCostPrice)
        val etPrice = findViewById<TextInputEditText>(R.id.etPrice)
        val etStock = findViewById<TextInputEditText>(R.id.etStock)
        val btnUpdate = findViewById<MaterialButton>(R.id.btnUpdate)

        val repo = ProductRepository()

        btnBack.setOnClickListener {
            finish()
        }


        val productId = intent.getStringExtra("PRODUCT_ID") ?: ""
        val productName = intent.getStringExtra("PRODUCT_NAME") ?: ""
        val productCost = intent.getDoubleExtra("PRODUCT_COST", 0.0)
        val productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0)
        val productStock = intent.getIntExtra("PRODUCT_STOCK", 0)

        // Preenchendo os campos automaticamente
        etName.setText(productName)
        etCostPrice.setText(productCost.toString())
        etPrice.setText(productPrice.toString())
        etStock.setText(productStock.toString())

        btnUpdate.setOnClickListener {
            val updatedProduct = Product(
                id = productId,
                name = etName.text.toString().trim(),
                costPrice = etCostPrice.text.toString().toDoubleOrNull() ?: 0.0,
                salePrice = etPrice.text.toString().toDoubleOrNull() ?: 0.0,
                stockQuantity = etStock.text.toString().toDoubleOrNull() ?: 0.0
            )

            if (updatedProduct.name.isNotEmpty()) {

                repo.updateProduct(updatedProduct, onSucess = {
                    Toast.makeText(this, "Produto atualizado!", Toast.LENGTH_SHORT).show()
                    finish()
                }, onFailure = {
                    Toast.makeText(this, "Erro ao atualizar", Toast.LENGTH_SHORT).show()
                })
            }
        }
    }
}