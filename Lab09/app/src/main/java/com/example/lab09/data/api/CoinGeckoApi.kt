package com.example.lab09.data.api

import com.example.lab09.data.model.CoinDetailResponse
import com.example.lab09.data.model.CoinResponse
import com.example.lab09.data.model.MarketChartResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoApi {

    @GET("coins/markets")
    suspend fun getCoinMarkets(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false
    ): List<CoinResponse>

    @GET("coins/{id}")
    suspend fun getCoinDetail(
        @Path("id") coinId: String,
        @Query("localization") localization: Boolean = false
    ): CoinDetailResponse

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") coinId: String,
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("days") days: Int = 7
    ): MarketChartResponse
}
