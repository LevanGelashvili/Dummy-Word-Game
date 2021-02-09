package com.adjarabet.user.domain.repository

import com.adjarabet.user.domain.entities.Word
import io.reactivex.Completable
import io.reactivex.Single

interface GameRepository {

    fun initOpponent(): Completable

    fun getOpponentsWord(myWord: Word): Single<String>

    //TODO: UNBIND
}