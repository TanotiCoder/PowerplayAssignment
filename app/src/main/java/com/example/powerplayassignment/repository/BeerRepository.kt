package com.example.powerplayassignment.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.powerplayassignment.apiService.BeerApiService
import com.example.powerplayassignment.dataModel.BeerDataModel
import com.example.powerplayassignment.pagingSource.BeerPagingSource


/**
 * Repository for fetching beer data from the remote API and storing it in the local cache.
 * Acts as a single source of truth for the app's beer data.
 */
class BeerRepository constructor(private val beerApiService: BeerApiService) {
    fun getBeerApiData(): LiveData<PagingData<BeerDataModel>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { BeerPagingSource(beerApiService) },
            initialKey = 1
        ).liveData
    }
}
