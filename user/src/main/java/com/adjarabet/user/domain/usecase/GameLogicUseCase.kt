package com.adjarabet.user.domain.usecase

import com.adjarabet.common.Constants

sealed class WordUseResult {
    data class Ok(val correctWord: String) : WordUseResult()
    data class Repeated(val repeatedWord: String) : WordUseResult()
    data class Invalid(val invalidWord: String) : WordUseResult()
    data class Conflicting(val playerWord: String, val opponentsWord: String) : WordUseResult()
    object GaveUp : WordUseResult()
}

class GameLogic {

    val wordSet = mutableSetOf<String>()

    fun validateSingleWord(word: String): WordUseResult {
        return when {
            word == Constants.BOT_GAVE_UP -> {
                WordUseResult.GaveUp
            }
            wordSet.contains(word) -> {
                WordUseResult.Repeated(word)
            }
            word.contains(" ") -> {
                WordUseResult.Invalid(word)
            }
            else -> {
                wordSet.add(word)
                WordUseResult.Ok(word)
            }
        }
    }

    fun validatePlayerInput(input: String): WordUseResult {
        val playerWords = input.split(" ")
        wordSet.forEachIndexed { index, opponentsWord ->
            if (opponentsWord != playerWords[index]) {
                return WordUseResult.Conflicting(playerWords[index], opponentsWord)
            }
        }
        return validateSingleWord(playerWords.last())
    }
}