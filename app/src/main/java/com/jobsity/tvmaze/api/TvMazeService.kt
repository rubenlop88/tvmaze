package com.jobsity.tvmaze.api

import com.jobsity.tvmaze.model.Episode
import com.jobsity.tvmaze.model.SearchResult
import com.jobsity.tvmaze.model.Show
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvMazeService {

    @GET("shows")
    suspend fun getShows(@Query("page") page: Int): List<Show>

    @GET("search/shows")
    suspend fun searchShows(@Query("q") query: String): List<SearchResult>

    @GET("shows/{id}")
    suspend fun getShow(@Path("id") id: Long): Show

    @GET("shows/{id}/episodes")
    suspend fun getEpisodes(@Path("id") id: Long): List<Episode>

    companion object {
        private const val BASE_URL = "https://api.tvmaze.com/"

        fun create(): TvMazeService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TvMazeService::class.java)
        }
    }
}