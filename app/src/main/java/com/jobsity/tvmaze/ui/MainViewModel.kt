package com.jobsity.tvmaze.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.jobsity.tvmaze.api.Show
import com.jobsity.tvmaze.api.ShowsPagingSource
import com.jobsity.tvmaze.api.TvMazeService

class MainViewModel : ViewModel() {

    val pagedShows: LiveData<PagingData<Show>> = Pager(PagingConfig(pageSize = 20)) {
        val backend = TvMazeService.create()
        ShowsPagingSource(backend, "")
    }.liveData.cachedIn(viewModelScope)
}