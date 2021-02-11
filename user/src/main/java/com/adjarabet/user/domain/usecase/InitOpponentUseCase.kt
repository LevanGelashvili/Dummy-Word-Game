package com.adjarabet.user.domain.usecase

import com.adjarabet.user.domain.repository.GameRepository
import com.adjarabet.user.utils.Result

class InitOpponentUseCase(private val gameRepository: GameRepository) {

    operator fun invoke(onOpponentInitialized: (Result<Unit>) -> Unit) {
        gameRepository.initOpponent(onOpponentInitialized)
    }
}