package com.adjarabet.bot.utils

import com.adjarabet.common.Constants
import kotlin.random.Random

class WordGenerator {

    private val wordsUsed = mutableSetOf<String>()

    fun generateWord(opponentsWord: String): String {

        val botDecidedToLose = Random.nextDouble() <= LOSING_PROBABILITY
        val wordLimitExceeded = wordsUsed.size >= WORD_LIMIT

        return if (botDecidedToLose || wordLimitExceeded) {
            Constants.BOT_GAVE_UP
        } else {
            wordsUsed.add(opponentsWord)
            generateRandomString().also {
                wordsUsed.add(it)
            }
        }
    }

    fun clearState() {
        wordsUsed.clear()
    }

    private fun generateRandomString(): String {
        val randomLengthRange = Random.nextInt(WORD_MIN_SIZE, WORD_MAX_SIZE)
        val charSetRange = ('a'..'z')

        val randomString = (1..randomLengthRange)
            .map { charSetRange.random() }
            .joinToString("")

        return if (wordsUsed.contains(randomString)) {
            generateRandomString()
        } else {
            randomString
        }
    }

    companion object {
        const val LOSING_PROBABILITY = 0.03
        const val WORD_LIMIT = 100
        const val WORD_MIN_SIZE = 2
        const val WORD_MAX_SIZE = 5
    }
}