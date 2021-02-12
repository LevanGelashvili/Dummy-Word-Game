package com.adjarabet.user.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adjarabet.user.domain.usecase.*
import com.adjarabet.user.utils.Result
import javax.inject.Inject

class GameViewModel @Inject constructor(
    initOpponentUseCase: InitOpponentUseCase,
    private val getOpponentsWordUseCase: GetOpponentsWordUseCase,
    private val clearOpponentStateUseCase: ClearOpponentStateUseCase,
    private val gameLogicUseCase: GameLogicUseCase
) : ViewModel() {

    private val _gameInitializedLiveData = MutableLiveData<Result<Unit>>()
    val gameInitializedLiveData: LiveData<Result<Unit>> get() = _gameInitializedLiveData

    private val _opponentWordLiveData = MutableLiveData<Result<String>>()
    val opponentWordLiveData: LiveData<Result<String>> get() = _opponentWordLiveData

    private val _clearOpponentStateLiveData = MutableLiveData<Result<Unit>>()
    val clearOpponentStateLiveData: LiveData<Result<Unit>> get() = _clearOpponentStateLiveData

    init {
        initOpponentUseCase {
            _gameInitializedLiveData.value = it
        }
    }

    fun sendWordToOpponent(word: String) {
        getOpponentsWordUseCase(word) {
            _opponentWordLiveData.value = it
        }
    }

    fun clearOpponentState() {
        clearOpponentStateUseCase {
            _clearOpponentStateLiveData.value = it
        }
    }

    fun validatePlayerInput(input: String): WordUseResult {
        return gameLogicUseCase.validatePlayerInput(input)
    }

    fun validateOpponentWord(word: String): WordUseResult {
        return gameLogicUseCase.validateSingleWord(word)
    }

    fun formatWordsForAdapter(): String {
        return gameLogicUseCase.wordSet.joinToString(separator = " ")
    }

    fun clearPlayerState() {
        gameLogicUseCase.clearState()
    }
}