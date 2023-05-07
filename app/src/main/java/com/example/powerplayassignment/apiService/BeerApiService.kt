package com.example.powerplayassignment.apiService

import com.example.powerplayassignment.dataModel.BeerDataModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


/**

This is an interface for accessing the Beer API service.

The interface defines a GET request method to retrieve beer data with two query parameters: page and per_page.

The method is defined as suspend function as it is used in a coroutine context.

The method returns a response object of a list of BeerDataModel instances.
 */

//https://api.punkapi.com/v2/beers?page=2&per_page=80
interface BeerApiService {
    @GET("beers")
    suspend fun getBeerData(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int = 20
    ): Response<List<BeerDataModel>>

    companion object {
        var beerApiService: BeerApiService? = null
        fun getInstance() : BeerApiService {
            if (beerApiService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.punkapi.com/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                beerApiService = retrofit.create(BeerApiService::class.java)
            }
            return beerApiService!!
        }
    }
}

