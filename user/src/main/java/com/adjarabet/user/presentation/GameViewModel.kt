package com.adjarabet.user.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adjarabet.user.data.MockGameRepositoryImpl
import com.adjarabet.user.domain.usecase.GetOpponentsWordUseCase
import com.adjarabet.user.domain.usecase.InitOpponentUseCase
import com.adjarabet.user.utils.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class GameViewModel : ViewModel() {

    private val gameRepository = MockGameRepositoryImpl()

    private val _gameSuccessfullyInitedLiveData = MutableLiveData<Result<Unit>>()
    val gameSuccessfullyInitedLiveData: LiveData<Result<Unit>> get() = _gameSuccessfullyInitedLiveData

    private val _opponentWordLiveData = MutableLiveData<Result<String>>()
    val opponentWordLiveData: LiveData<Result<String>> get() = _opponentWordLiveData

    init {
        initOpponent()
    }

    private fun initOpponent() {
        InitOpponentUseCase(gameRepository).invoke {
            _gameSuccessfullyInitedLiveData.value = it
        }
    }

    //TODO: add loading state
    fun sendWordToOpponent(word: String) {
        GetOpponentsWordUseCase(gameRepository).invoke(word) {
            _opponentWordLiveData.value = it
        }
    }

    fun isValidWord(word: String): Boolean {
        return true
    }

}