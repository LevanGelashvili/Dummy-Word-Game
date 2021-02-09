package com.adjarabet.user.domain.usecase

import com.adjarabet.user.domain.repository.GameRepository
import io.reactivex.Completable

class InitOpponentUseCase(private val gameRepository: GameRepository) {

    operator fun invoke(): Completable {
        return gameRepository.initOpponent()
    }
}