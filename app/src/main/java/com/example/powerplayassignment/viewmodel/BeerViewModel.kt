package com.example.powerplayassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import com.example.powerplayassignment.dataModel.BeerDataModel
import com.example.powerplayassignment.repository.BeerRepository


/**
 * This class serves as the ViewModel for the BeerFragment. It contains the logic
 * to fetch the beer data from the repository and to provide the data to the UI. It
 * also stores the current state of the UI such as the selected beer item and the
 * page number for fetching the data. It uses LiveData to observe changes in the
 * data and to notify the UI accordingly. The class also defines methods to handle
 * user interactions such as selecting a beer item or refreshing the data.
 */

class BeerViewModel constructor(private val beerRepository: BeerRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    fun getBeerData(): LiveData<PagingData<BeerDataModel>> {
        return beerRepository.getBeerApiData()
    }
}


class BeerViewModelFactory constructor(private val repository: BeerRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(BeerViewModel::class.java)) {
            BeerViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}

