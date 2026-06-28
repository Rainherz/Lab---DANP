package com.example.lab09.data.repository

import com.example.lab09.data.model.CoinDetailResponse
import com.example.lab09.data.model.CoinResponse
import com.example.lab09.data.model.MarketChartResponse

interface CoinRepository {
    suspend fun getCoins(): List<CoinResponse>
    suspend fun getCoinDetail(coinId: String): CoinDetailResponse
    suspend fun getMarketChart(coinId: String, days: Int = 7): MarketChartResponse
}
