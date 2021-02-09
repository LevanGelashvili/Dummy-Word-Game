package com.adjarabet.user.domain.usecase

import com.adjarabet.user.domain.entities.Word
import com.adjarabet.user.domain.repository.GameRepository
import io.reactivex.Single

class GetOpponentsWordUseCase(private val gameRepository: GameRepository) {

    operator fun invoke(myWord: Word): Single<String> {
        return gameRepository.getOpponentsWord(myWord)
    }
}