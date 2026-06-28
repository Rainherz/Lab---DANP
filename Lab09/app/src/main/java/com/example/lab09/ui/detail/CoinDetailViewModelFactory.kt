package com.example.lab09.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lab09.data.repository.CoinRepositoryImpl

class CoinDetailViewModelFactory(
    private val coinId: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoinDetailViewModel::class.java)) {
            return CoinDetailViewModel(
                coinId = coinId,
                repository = CoinRepositoryImpl()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
