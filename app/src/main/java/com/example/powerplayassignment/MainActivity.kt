package com.example.powerplayassignment

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.powerplayassignment.adapter.BeerPagerAdapter
import com.example.powerplayassignment.apiService.BeerApiService
import com.example.powerplayassignment.databinding.ActivityMainBinding
import com.example.powerplayassignment.databinding.BeerAdapterBinding
import com.example.powerplayassignment.repository.BeerRepository
import com.example.powerplayassignment.viewmodel.BeerViewModel
import com.example.powerplayassignment.viewmodel.BeerViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: BeerViewModel
    private val adapter = BeerPagerAdapter(this)
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val beerApiService = BeerApiService.getInstance()
        val beerRepository = BeerRepository(beerApiService)
        binding.recyclerview.adapter = adapter
        viewModel = ViewModelProvider(
            this,
            BeerViewModelFactory(beerRepository)
        ).get(BeerViewModel::class.java)


        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        adapter.addLoadStateListener { loadState ->
            // show empty list
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading)
                binding.progressDialog.isVisible = true
            else {
                binding.progressDialog.isVisible = false
                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(this, it.error.toString(), Toast.LENGTH_LONG).show()
                }

            }
        }
        lifecycleScope.launch {
            viewModel.getBeerData().observe(this@MainActivity) {
                it?.let {
                    adapter.submitData(lifecycle, it)
                }
            }
        }
    }
}

