package com.adjarabet.user.domain.usecase

import com.adjarabet.common.Constants

sealed class WordUseResult {
    data class Ok(val correctWord: String) : WordUseResult()
    data class Repeated(val repeatedWord: String) : WordUseResult()
    data class Invalid(val invalidWord: String) : WordUseResult()
    data class Conflicting(val playerWord: String, val opponentsWord: String) : WordUseResult()
    object GaveUp : WordUseResult()
}

class GameLogicUseCase {

    val wordSet = mutableSetOf<String>()

    fun validateSingleWord(word: String): WordUseResult {
        return when {
            word == Constants.BOT_GAVE_UP -> {
                WordUseResult.GaveUp
            }
            wordSet.contains(word) -> {
                WordUseResult.Repeated(word)
            }
            word.isBlank() || word.contains(" ") -> {
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
        val playerUsedASpace = playerWords.size > wordSet.size + 1

        if (playerUsedASpace) {
            return WordUseResult.Invalid(input)
        }
        wordSet.forEachIndexed { index, opponentsWord ->
            if (index >= playerWords.size) {
                return WordUseResult.Conflicting(playerWords[index - 1], opponentsWord)
            } else if (opponentsWord != playerWords[index]) {
                return WordUseResult.Conflicting(playerWords[index], opponentsWord)
            }
        }
        return validateSingleWord(playerWords.last())
    }

    fun clearState() {
        wordSet.clear()
    }
}