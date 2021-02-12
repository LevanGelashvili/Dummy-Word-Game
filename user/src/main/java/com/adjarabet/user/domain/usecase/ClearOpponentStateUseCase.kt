package com.adjarabet.user.domain.usecase

import com.adjarabet.user.domain.repository.GameRepository
import com.adjarabet.user.utils.Result
import javax.inject.Inject

class ClearOpponentStateUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    operator fun invoke(onOpponentShutdown: (Result<Unit>) -> Unit) {
        gameRepository.clearOpponentState(onOpponentShutdown)
    }
}