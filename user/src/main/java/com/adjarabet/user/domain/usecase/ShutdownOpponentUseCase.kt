package com.adjarabet.user.domain.usecase

import com.adjarabet.user.domain.repository.GameRepository

class ShutdownOpponentUseCase(private val gameRepository: GameRepository) {

    operator fun invoke() {
        gameRepository.shutdownOpponent()
    }
}