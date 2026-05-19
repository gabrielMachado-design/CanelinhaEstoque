package com.example.canelinhaestoque.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.canelinhaestoque.data.repository.SaleRepository
import com.example.canelinhaestoque.databinding.ActivityReportsBinding
import com.example.canelinhaestoque.ui.adapters.SalesAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ReportsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportsBinding
    private val salesAdapter = SalesAdapter()

    @Inject
    lateinit var saleRepository: SaleRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadTodaySales()
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        binding.rvSalesHistory.apply {
            layoutManager = LinearLayoutManager(this@ReportsActivity)
            adapter = salesAdapter
        }
    }

    private fun loadTodaySales() {
        val calendar = Calendar.getInstance()


        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endOfDay = calendar.timeInMillis

        saleRepository.getSalesByPeriod(startOfDay, endOfDay) { sales ->
            val totalAmount = sales.sumOf { it.totalAmount }
            val count = sales.size

            binding.tvTotalSales.text = String.format("R$ %.2f", totalAmount)
            binding.tvSalesCount.text = "$count vendas realizadas"


            salesAdapter.updateList(sales)
        }
    }
}