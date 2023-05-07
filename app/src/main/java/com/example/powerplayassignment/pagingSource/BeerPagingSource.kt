package com.example.powerplayassignment.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.powerplayassignment.apiService.BeerApiService
import com.example.powerplayassignment.dataModel.BeerDataModel
import retrofit2.HttpException


// The PagingSource class is responsible for providing data to the PagingData object in chunks, based on the
// pagination logic. It abstracts the details of loading data from the data source and handles page loading
// and error states. It receives a key (in this case, the page number) to load the corresponding chunk of data
// from the data source, and it returns a LoadResult object that represents the loaded data or an error if any.


class BeerPagingSource(private val beerApiService: BeerApiService) :
    PagingSource<Int, BeerDataModel>() {
    override fun getRefreshKey(state: PagingState<Int, BeerDataModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BeerDataModel> {
        return try {
            val position = params.key ?: 1
            val response = beerApiService.getBeerData(position)
            LoadResult.Page(
                data = response.body()!!,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.body()!!.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}

