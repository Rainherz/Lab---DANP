package com.example.domain.usecase

import com.example.domain.model.UserTechnology
import com.example.domain.repository.AlertRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserTechnologiesUseCase @Inject constructor(
    private val repository: AlertRepository
) {
    operator fun invoke(): Flow<List<UserTechnology>> = repository.getUserTechnologies()
}
