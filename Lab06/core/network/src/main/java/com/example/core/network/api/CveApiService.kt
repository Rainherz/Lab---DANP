package com.example.core.network.api

import retrofit2.http.GET
import retrofit2.http.Query

interface CveApiService {
    @GET(".")
    suspend fun getCves(
        @Query("keywordSearch") keyword: String? = null,
        @Query("resultsPerPage") resultsPerPage: Int = 50
    ): NvdResponseDto
}

