package com.tuapp.cineapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tuapp.cineapp.data.remote.TmdbApiService
import com.tuapp.cineapp.domain.model.Movie

class MoviePagingSource(
    private val api: TmdbApiService,
    private val apiKey: String
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = api.getPopularMovies(apiKey, page = page)
            val movies = response.results.map { it.toDomain() }
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty() || page >= response.totalPages) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
