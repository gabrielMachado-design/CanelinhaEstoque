package com.example.canelinhaestoque.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.compose.runtime.snapshotFlow
import com.example.canelinhaestoque.databinding.ActivitySaleBinding
import com.example.canelinhaestoque.ui.adapters.SaleItemAdapter
import com.example.canelinhaestoque.ui.adapters.SearchSuggestionAdapter
import com.example.canelinhaestoque.viewmodel.SaleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SaleActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySaleBinding
    private val viewModel: SaleViewModel by viewModels()
    private lateinit var cartAdapter: SaleItemAdapter
    private lateinit var searchAdapter: SearchSuggestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()
        setupSearch()
        setupListeners()
        observeViewModel()

        viewModel.loadInventory()
    }

    private fun setupRecyclerViews() {

        cartAdapter = SaleItemAdapter(
            onQuantityChanged = { id, novaQtd ->
                viewModel.updateQuantity(id, novaQtd)
            },
            onRemoveItem = { id ->
                viewModel.removeItem(id)
            }
        )
        binding.rvCartItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartItems.adapter = cartAdapter


        searchAdapter = SearchSuggestionAdapter { product ->
            viewModel.addProduct(product)
            binding.etSearch.text?.clear()
            binding.rvSearchResults.visibility = View.GONE
        }
        binding.rvSearchResults.layoutManager = LinearLayoutManager(this)
        binding.rvSearchResults.adapter = searchAdapter
    }

    private fun setupSearch() {
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().lowercase().trim()
            if (query.isNotEmpty()) {
                val filtered = viewModel.allProducts.filter {
                    it.name.lowercase().contains(query)
                }
                if (filtered.isNotEmpty()) {
                    searchAdapter.updateList(filtered)
                    binding.rvSearchResults.visibility = View.VISIBLE
                } else {
                    binding.rvSearchResults.visibility = View.GONE
                }
            } else {
                binding.rvSearchResults.visibility = View.GONE
            }
        }
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            if (viewModel.items.isEmpty()) {
                Toast.makeText(this, "Adicione itens à venda!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.finalizeSale(
                onSuccess = {
                    Toast.makeText(this, "Venda salva com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                },
                onFailure = { Toast.makeText(this, "Erro: ${it.message}", Toast.LENGTH_LONG).show() }
            )
        }
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            snapshotFlow { viewModel.items }.collect { cartAdapter.updateList(it) }
        }
        lifecycleScope.launch {
            snapshotFlow { viewModel.getTotal() }.collect { total ->
                binding.tvTotal.text = String.format("Total: R$ %.2f", total)
            }
        }
    }
}
