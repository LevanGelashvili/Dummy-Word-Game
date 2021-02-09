package com.adjarabet.user.data

import com.adjarabet.user.domain.entities.Word
import com.adjarabet.user.domain.repository.GameRepository
import io.reactivex.Completable
import io.reactivex.Single

class MockGameRepositoryImpl: GameRepository {

    override fun initOpponent(): Completable {
        return Completable.complete()
    }

    override fun getOpponentsWord(myWord: Word): Single<String> {
        return Single.just("$myWord Duh")
    }
}