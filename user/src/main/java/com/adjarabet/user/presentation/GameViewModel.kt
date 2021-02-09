package com.adjarabet.user.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adjarabet.user.data.MockGameRepositoryImpl
import com.adjarabet.user.domain.entities.Word
import com.adjarabet.user.domain.usecase.GetOpponentsWordUseCase
import com.adjarabet.user.domain.usecase.InitOpponentUseCase
import com.adjarabet.user.utils.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class GameViewModel : ViewModel() {

    private val gameRepository = MockGameRepositoryImpl()

    private val _gameSuccessfullyInitedLiveData = MutableLiveData<Boolean>()
    val gameSuccessfullyInitedLiveData: LiveData<Boolean> get() = _gameSuccessfullyInitedLiveData

    private val _opponentWordLiveData = MutableLiveData<Result<String>>()
    val opponentWordLiveData: LiveData<Result<String>> get() = _opponentWordLiveData

    init {
        initOpponent()
    }

    private fun initOpponent() {
        InitOpponentUseCase(gameRepository).invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    _gameSuccessfullyInitedLiveData.value = true
                },
                onError = {
                    _gameSuccessfullyInitedLiveData.value = false
                }
            )
    }

    //TODO: add loading state
    fun sendWordToOpponent(word: String) {
        GetOpponentsWordUseCase(gameRepository).invoke(Word(word))
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    _opponentWordLiveData.value = Result.Success(it)
                },
                onError = {
                    _opponentWordLiveData.value = Result.Error(it)
                }
            )
    }

    fun isValidWord(word: String): Boolean {
        return true
    }

}