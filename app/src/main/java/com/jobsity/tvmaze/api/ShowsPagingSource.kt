package com.jobsity.tvmaze.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException

class ShowsPagingSource(
    private val backend: TvMazeService,
    val query: String
) : PagingSource<Int, Show>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Show> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 0
            val response = backend.getShows(nextPageNumber)
            return LoadResult.Page(
                data = response,
                prevKey = null, // Only paging forward.
                // The API and returns an empty list with code 200 for the last page
                nextKey = if (response.isNotEmpty()) nextPageNumber + 1 else null
            )
        } catch (e: HttpException) {
            // TODO https://www.tvmaze.com/api#show-index says:
            //  simply increment the page number by 1 until you receive a HTTP 404 response
            //  code, which indicates that you've reached the end of the list.
            //  Should I return LoadResult.Page() when the code is 404? I tested the API and it
            //  seems to return an empty list with code 200
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    // Copied from https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data
    override fun getRefreshKey(state: PagingState<Int, Show>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}