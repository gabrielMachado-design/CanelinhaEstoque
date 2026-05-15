package com.example.canelinhaestoque.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.canelinhaestoque.data.model.SaleItem
import com.example.canelinhaestoque.databinding.ItemCartSaleBinding

class SaleItemAdapter(
    private val onQuantityChanged: (String, Double) -> Unit,
    private val onRemoveItem: (String) -> Unit
) : RecyclerView.Adapter<SaleItemAdapter.ViewHolder>() {

    private var items: List<SaleItem> = emptyList()

    fun updateList(newList: List<SaleItem>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartSaleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(private val binding: ItemCartSaleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SaleItem) {
            binding.tvProductName.text = item.name
            binding.tvUnitPrice.text = String.format("Unit: R$ %.2f", item.unitPrice)
            binding.tvSubtotal.text = String.format("R$ %.2f", item.unitPrice * item.quantity)


            binding.etQuantity.setText(item.quantity.toString())

            binding.btnPlus.setOnClickListener {
                onQuantityChanged(item.productId, item.quantity + 1)
            }

            binding.btnMinus.setOnClickListener {
                if (item.quantity > 1) {
                    onQuantityChanged(item.productId, item.quantity - 1)
                } else {
                    onRemoveItem(item.productId)
                }
            }

            binding.etQuantity.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) { // Quando o usuário termina de digitar
                    val valInput = binding.etQuantity.text.toString().toDoubleOrNull() ?: 1.0
                    if (valInput <= 0) onRemoveItem(item.productId)
                    else onQuantityChanged(item.productId, valInput)
                }
            }
        }
    }
}