package com.example.canelinhaestoque.data.repository

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.example.canelinhaestoque.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream

class ProductRepository {

    private val db = FirebaseFirestore.getInstance()


    fun getProducts(callback: (List<Product>) -> Unit) {
        db.collection("produtos")
            .addSnapshotListener { result, error ->
                if (error != null) {
                    callback(emptyList())
                    return@addSnapshotListener
                }

                val productList = mutableListOf<Product>()
                for (document in result!!) {
                    val product = Product(
                        id = document.id,
                        // Ajustado para ler os nomes que você salva no addProduct
                        name = document.getString("nome") ?: "",
                        description = document.getString("descrição") ?: "",
                        costPrice = document.getDouble("preco_custo") ?: 0.0,
                        salePrice = document.getDouble("preco_venda") ?: 0.0,
                        stockQuantity = document.getDouble("estoque") ?: 0.0,
                        photoUrl = document.getString("foto_url") ?: ""
                    )
                    productList.add(product)
                }
                callback(productList)
            }
    }

    fun generatePdfReport(context: Context, productList: List<Product>) {
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val titlePaint = Paint()


        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas


        titlePaint.textSize = 25f
        titlePaint.isFakeBoldText = true
        canvas.drawText("Relatório de Estoque - Canelinha", 40f, 50f, titlePaint)


        paint.textSize = 16f
        paint.isFakeBoldText = true
        canvas.drawText("Produto", 40f, 100f, paint)
        canvas.drawText("Quantidade", 400f, 100f, paint)
        canvas.drawLine(40f, 115f, 550f, 115f, paint)


        paint.isFakeBoldText = false
        paint.textSize = 14f
        var yPosition = 150f

        for (product in productList) {
            canvas.drawText(product.name, 40f, yPosition, paint)
            canvas.drawText(product.stockQuantity.toString(), 400f, yPosition, paint)
            yPosition += 30f

            if (yPosition > 800f) break
        }

        pdfDocument.finishPage(page)


        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "RelatorioEstoque.pdf"
        )

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(context, "PDF Salvo com sucesso!", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao gerar PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }

    fun addProduct(product: Product, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val data = hashMapOf(
            "nome" to product.name,
            "descrição" to product.description,
            "preco_custo" to product.costPrice,
            "preco_venda" to product.salePrice,
            "estoque" to product.stockQuantity,
            "foto_url" to product.photoUrl
        )

        db.collection("produtos")
            .add(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun updateProduct(product: Product, onSucess: () -> Unit, onFailure: (Exception) -> Unit) {
        val data = hashMapOf(
            "nome" to product.name,
            "descrição" to product.description,
            "preco_custo" to product.costPrice,
            "preco_venda" to product.salePrice,
            "estoque" to product.stockQuantity,
            "foto_url" to product.photoUrl
        )
        db.collection("produtos")
            .document(product.id)
            .update(data as Map<String, Any>)
            .addOnSuccessListener { onSucess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun deleteProduct(productId: String, onSucess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("produtos")
            .document(productId)
            .delete()
            .addOnSuccessListener { onSucess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }
}

