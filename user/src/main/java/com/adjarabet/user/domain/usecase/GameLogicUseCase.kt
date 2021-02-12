package com.adjarabet.user.domain.usecase

import com.adjarabet.common.Constants

sealed class WordValidation {
    data class Ok(val correctWord: String) : WordValidation()
    data class Repeated(val repeatedWord: String) : WordValidation()
    data class Invalid(val invalidWord: String) : WordValidation()
    data class Conflicting(val playerWord: String, val opponentsWord: String) : WordValidation()
    object GaveUp : WordValidation()
}

class GameLogicUseCase {

    val wordSet = mutableSetOf<String>()

    fun validateSingleWord(word: String): WordValidation {
        return when {
            word == Constants.BOT_GAVE_UP -> {
                WordValidation.GaveUp
            }
            wordSet.contains(word) -> {
                WordValidation.Repeated(word)
            }
            word.isBlank() || word.contains(" ") -> {
                WordValidation.Invalid(word)
            }
            else -> {
                wordSet.add(word)
                WordValidation.Ok(word)
            }
        }
    }

    fun validatePlayerInput(input: String): WordValidation {

        val playerWords = input.split(" ")
        val playerUsedASpace = playerWords.size > wordSet.size + 1

        if (playerUsedASpace) {
            return WordValidation.Invalid(input)
        }
        wordSet.forEachIndexed { index, opponentsWord ->
            if (index >= playerWords.size) {
                return WordValidation.Conflicting(playerWords[index - 1], opponentsWord)
            } else if (opponentsWord != playerWords[index]) {
                return WordValidation.Conflicting(playerWords[index], opponentsWord)
            }
        }
        return validateSingleWord(playerWords.last())
    }

    fun clearState() {
        wordSet.clear()
    }
}