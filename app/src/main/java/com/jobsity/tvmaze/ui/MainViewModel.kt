package com.jobsity.tvmaze.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.jobsity.tvmaze.api.TvMazeService

class MainViewModel : ViewModel() {

    val shows: LiveData<Result> = liveData {
        try {
            val api = TvMazeService.create()
            emit(Result.Success(api.getShows(1)))
        } catch(ioException: Exception) {
            emit(Result.Error(ioException))
        }
    }
}