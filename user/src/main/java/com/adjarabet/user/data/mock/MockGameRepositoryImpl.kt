package com.adjarabet.user.data.mock

import com.adjarabet.user.domain.repository.GameRepository
import com.adjarabet.user.utils.Result

class MockGameRepositoryImpl : GameRepository {

    override fun initOpponent(onSuccessfullyInitialized: (Result<Unit>) -> Unit) {

    }

    override fun getOpponentsWord(
        myWord: String,
        onOpponentWordReceived: (Result<String>) -> Unit
    ) {
        onOpponentWordReceived(Result.Success("$myWord Duh"))
    }
}