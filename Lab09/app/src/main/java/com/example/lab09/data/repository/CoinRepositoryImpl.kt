package com.example.lab09.data.repository

import com.example.lab09.data.api.CoinGeckoApi
import com.example.lab09.data.api.RetrofitInstance
import com.example.lab09.data.model.CoinDetailResponse
import com.example.lab09.data.model.CoinResponse
import com.example.lab09.data.model.MarketChartResponse

class CoinRepositoryImpl(
    private val api: CoinGeckoApi = RetrofitInstance.api
) : CoinRepository {

    override suspend fun getCoins(): List<CoinResponse> {
        return api.getCoinMarkets()
    }

    override suspend fun getCoinDetail(coinId: String): CoinDetailResponse {
        return api.getCoinDetail(coinId)
    }

    override suspend fun getMarketChart(coinId: String, days: Int): MarketChartResponse {
        return api.getMarketChart(coinId, days = days)
    }
}
