package com.adjarabet.user.domain.usecase

import com.adjarabet.user.domain.repository.GameRepository
import com.adjarabet.user.utils.Result

class InitOpponentUseCase(private val gameRepository: GameRepository) {

    operator fun invoke(onSuccessfullyInitialized: (Result<Unit>) -> Unit) {
        gameRepository.initOpponent(onSuccessfullyInitialized)
    }
}