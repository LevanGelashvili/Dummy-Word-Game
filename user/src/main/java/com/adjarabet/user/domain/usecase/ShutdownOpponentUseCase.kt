package com.adjarabet.user.domain.usecase

import com.adjarabet.user.domain.repository.GameRepository
import javax.inject.Inject

class ShutdownOpponentUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    operator fun invoke() {
        gameRepository.shutdownOpponent()
    }
}