package com.jobsity.tvmaze.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jobsity.tvmaze.model.Show

class ShowsPagingSource(
    private val backend: TvMazeService,
    private val query: String?,
) : PagingSource<Int, Show>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Show> {
        return try {
            if (query == null || query.isEmpty()) {
                val page = params.key ?: 0
                val response = backend.getShows(page)
                val nextPage = if (response.isEmpty()) null else page + 1
                LoadResult.Page(data = response, prevKey = null, nextKey = nextPage)
            } else {
                val response = backend.searchShows(query).map { it.show }
                LoadResult.Page(data = response, prevKey = null, nextKey = null)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Show>): Int? {
        return null
    }
}