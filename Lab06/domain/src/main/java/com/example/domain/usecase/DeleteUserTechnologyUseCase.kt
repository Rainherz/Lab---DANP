package com.example.domain.usecase

import com.example.domain.repository.AlertRepository
import javax.inject.Inject

class DeleteUserTechnologyUseCase @Inject constructor(
    private val repository: AlertRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteUserTechnology(id)
    }
}
