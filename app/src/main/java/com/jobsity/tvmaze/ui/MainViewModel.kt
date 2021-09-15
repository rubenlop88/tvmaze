package com.jobsity.tvmaze.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.jobsity.tvmaze.model.Show
import com.jobsity.tvmaze.api.ShowsPagingSource
import com.jobsity.tvmaze.api.TvMazeService

class MainViewModel : ViewModel() {

    private val backend = TvMazeService.create()

    fun getShows(query: String?): LiveData<PagingData<Show>> {
        val pager = Pager(PagingConfig(pageSize = 20)) { ShowsPagingSource(backend, query) }
        return pager.liveData.cachedIn(viewModelScope)
    }
}