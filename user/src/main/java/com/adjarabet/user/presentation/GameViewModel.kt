package com.adjarabet.user.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adjarabet.user.data.bot.BotGameRepositoryImpl
import com.adjarabet.user.domain.usecase.GetOpponentsWordUseCase
import com.adjarabet.user.domain.usecase.InitOpponentUseCase
import com.adjarabet.user.utils.Result

class GameViewModel : ViewModel() {

    private val gameRepository = BotGameRepositoryImpl()

    private val _gameInitializedLiveData = MutableLiveData<Result<Unit>>()
    val gameInitializedLiveData: LiveData<Result<Unit>> get() = _gameInitializedLiveData

    private val _opponentWordLiveData = MutableLiveData<Result<String>>()
    val opponentWordLiveData: LiveData<Result<String>> get() = _opponentWordLiveData

    init {
        InitOpponentUseCase(gameRepository).invoke {
            _gameInitializedLiveData.value = it
        }
    }

    //TODO: add loading state
    fun sendWordToOpponent(word: String) {
        GetOpponentsWordUseCase(gameRepository).invoke(word) {
            _opponentWordLiveData.value = it
        }
    }
}