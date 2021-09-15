package com.jobsity.tvmaze.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.jobsity.tvmaze.api.TvMazeService
import com.jobsity.tvmaze.model.Show

class ShowViewModel : ViewModel() {

    private val backend = TvMazeService.create()

    fun getShow(id: Long): LiveData<Show> {
        return liveData {
            emit(backend.getShow(id))
        }
    }
}