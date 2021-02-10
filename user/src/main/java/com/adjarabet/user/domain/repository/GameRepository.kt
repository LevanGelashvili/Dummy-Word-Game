package com.adjarabet.user.domain.repository

import com.adjarabet.user.utils.Result

interface GameRepository {

    fun initOpponent(onOpponentInitialized: (Result<Unit>) -> Unit)

    fun getOpponentsWord(myWord: String, onOpponentWordReceived: (Result<String>) -> Unit)

    //TODO: UNBIND
}