package com.adjarabet.common.game

open class GameLogic {

    enum class WordUseResult {
        OK, REPEATED, INVALID
    }

    private val words = mutableSetOf<String>()

    fun addWordToUsed(word: String) {
        words.add(word)
    }

    fun canUseWord(word: String): WordUseResult {
        return if (words.contains(word)) {
            WordUseResult.REPEATED
        } else {
            WordUseResult.OK
        }
    }

    fun isValidWord(word: String): WordUseResult {
        return if(true) {
            WordUseResult.OK
        } else {
            WordUseResult.INVALID
        }
    }
}