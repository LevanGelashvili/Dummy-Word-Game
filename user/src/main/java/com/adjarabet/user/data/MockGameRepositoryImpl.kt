package com.adjarabet.user.data

import com.adjarabet.user.domain.repository.GameRepository
import com.adjarabet.user.utils.Result
import io.reactivex.Completable
import io.reactivex.Single

class MockGameRepositoryImpl : GameRepository {

    override fun initOpponent(onOpponentInitialized: (Result<Unit>) -> Unit) {
        onOpponentInitialized(Result.Success(Unit))
    }

    override fun getOpponentsWord(
        myWord: String,
        onOpponentWordReceived: (Result<String>) -> Unit
    ) {
        onOpponentWordReceived(Result.Success("$myWord Duh"))
    }
}