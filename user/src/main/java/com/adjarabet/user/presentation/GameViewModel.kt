package com.adjarabet.user.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adjarabet.user.data.bot.BotGameRepositoryImpl
import com.adjarabet.user.domain.usecase.*
import com.adjarabet.user.utils.Result

class GameViewModel : ViewModel() {

    private val gameRepository = BotGameRepositoryImpl()
    private val gameLogic = GameLogic()

    private val _gameInitializedLiveData = MutableLiveData<Result<Unit>>()
    val gameInitializedLiveData: LiveData<Result<Unit>> get() = _gameInitializedLiveData

    private val _opponentWordLiveData = MutableLiveData<Result<String>>()
    val opponentWordLiveData: LiveData<Result<String>> get() = _opponentWordLiveData

    init {
        InitOpponentUseCase(gameRepository).invoke {
            _gameInitializedLiveData.value = it
        }
    }

    fun sendWordToOpponent(word: String) {
        GetOpponentsWordUseCase(gameRepository).invoke(word) {
            _opponentWordLiveData.value = it
        }
    }

    fun shutdownOpponent() {
        ShutdownOpponentUseCase(gameRepository).invoke()
    }

    fun validatePlayerInput(input: String): WordUseResult {
        return gameLogic.validatePlayerInput(input)
    }

    fun validateOpponentWord(word: String): WordUseResult {
        return gameLogic.validateSingleWord(word)
    }

    fun formatWordsForAdapter(): String {
        return gameLogic.wordSet.joinToString(separator = " ")
    }
}