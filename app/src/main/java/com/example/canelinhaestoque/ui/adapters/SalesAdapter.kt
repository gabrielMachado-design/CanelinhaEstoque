package com.example.canelinhaestoque.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.canelinhaestoque.data.model.Sale
import com.example.canelinhaestoque.databinding.ItemSaleHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class SalesAdapter : RecyclerView.Adapter<SalesAdapter.ViewHolder>() {

    private var sales: List<Sale> = emptyList()

    fun updateList(newList: List<Sale>) {

        sales = newList.sortedByDescending { it.date }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSaleHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sales[position])
    }

    override fun getItemCount() = sales.size

    class ViewHolder(private val binding: ItemSaleHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sale: Sale) {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val hour = sdf.format(Date(sale.date))

            binding.tvSaleTime.text = hour
            binding.tvItemCount.text = "${sale.items.size} itens"
            binding.tvSaleTotal.text = String.format("R$ %.2f", sale.totalAmount)
        }
    }
}