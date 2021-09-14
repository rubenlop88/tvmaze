package com.jobsity.tvmaze.ui

import com.jobsity.tvmaze.api.Show
import java.lang.Exception

sealed class Result {
    data class Success(val data: List<Show>) : Result()
    data class Error(val error: Exception) : Result()
}