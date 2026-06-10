package com.example.domain.usecase

import com.example.domain.repository.AlertRepository
import javax.inject.Inject

class AddUserTechnologyUseCase @Inject constructor(
    private val repository: AlertRepository
) {
    suspend operator fun invoke(name: String, version: String) {
        repository.addUserTechnology(name, version)
    }
}
