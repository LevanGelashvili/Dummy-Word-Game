package com.adjarabet.user.domain.usecase

import com.adjarabet.user.domain.repository.GameRepository
import com.adjarabet.user.utils.Result
import javax.inject.Inject

class GetOpponentsWordUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    operator fun invoke(myWord: String, onOpponentWordReceived: (Result<String>) -> Unit) {
        gameRepository.getOpponentsWord(myWord, onOpponentWordReceived)
    }
}